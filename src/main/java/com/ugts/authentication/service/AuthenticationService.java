package com.ugts.authentication.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.ugts.authentication.dto.request.*;
import com.ugts.authentication.dto.response.IntrospectResponse;
import com.ugts.authentication.dto.response.LoginResponse;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;

public interface AuthenticationService {
    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    String generateToken(User user);

    LoginResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;

    String buildScope(User user);

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    void forgotPassword(ForgotPasswordRequest request);

    void changePassword(String userId, ChangePasswordRequest request);
}
