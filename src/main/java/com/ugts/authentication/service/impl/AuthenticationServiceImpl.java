package com.ugts.authentication.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ugts.authentication.dto.request.*;
import com.ugts.authentication.dto.response.IntrospectResponse;
import com.ugts.authentication.dto.response.LoginResponse;
import com.ugts.authentication.entity.InvalidToken;
import com.ugts.authentication.repository.InvalidTokenRepository;
import com.ugts.authentication.service.AuthenticationService;
import com.ugts.constant.PredefinedRole;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.Role;
import com.ugts.user.entity.User;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.RoleRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    InvalidTokenRepository invalidTokenRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected long REFRESH_DURATION;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(request.getPhoneNumber()))) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = userMapper.register(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        var user = userRepository
                .findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);

        return LoginResponse.builder().accessToken(token).authenticated(true).build();
    }

    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getPhoneNumber())
                .issuer("ugts.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not create token", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJwt = verifyToken(request.getAccessToken(), true);

        var jit = signedJwt.getJWTClaimsSet().getJWTID();
        var expireTime = signedJwt.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken =
                InvalidToken.builder().id(jit).expireTime(expireTime).build();

        invalidTokenRepository.save(invalidToken);

        var phoneNumber = signedJwt.getJWTClaimsSet().getSubject();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var token = generateToken(user);

        return LoginResponse.builder().accessToken(token).authenticated(true).build();
    }

    @Override
    public String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expireTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESH_DURATION, ChronoUnit.HOURS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expireTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getAccessToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expireTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidToken invalidToken =
                    InvalidToken.builder().id(jit).expireTime(expireTime).build();

            invalidTokenRepository.save(invalidToken);
        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        if(Boolean.FALSE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        String newPassword = passwordEncoder.encode(request.getConfirmPassword());
        userRepository.updatePassword(request.getEmail(), newPassword);
    }

    @Override
    public void changePassword(String userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        String newPassword = passwordEncoder.encode(request.getNewPassword());
        userRepository.changePassword(userId, newPassword);
    }
}
