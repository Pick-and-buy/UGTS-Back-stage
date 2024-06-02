package com.ugts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.ugts.authentication.dto.request.*;
import com.ugts.authentication.dto.response.IntrospectResponse;
import com.ugts.authentication.dto.response.LoginResponse;
import com.ugts.authentication.entity.InvalidToken;
import com.ugts.authentication.repository.InvalidTokenRepository;
import com.ugts.authentication.service.AuthenticationService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.Role;
import com.ugts.user.entity.User;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.RoleRepository;
import com.ugts.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
class UserServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean // mock to user repository
    private UserRepository userRepository;

    private RegisterRequest request;
    private UserResponse response;
    private User user;

    @MockBean
    private LoginResponse loginResponse;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;


    @Mock
    InvalidTokenRepository invalidTokenRepository;
    @Mock
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @ConditionalOnProperty(prefix = "spring", value = "datasource.driverClassName",havingValue = "org.postgresql.Driver")
    @BeforeEach
    public void initData() {
        LocalDate dob = LocalDate.of(2000, 1, 1);

        request = RegisterRequest.builder()
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .password("Quang09122002@")
                .dob(dob)
                .phoneNumber("0563016466")
                .build();

        response = UserResponse.builder()
                .id("bf1ca931-8685-4ea7-b554-85926ec6e9e5")
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .dob(dob)
                .phoneNumber("0563016466")
                .build();

        user = User.builder()
                .id("bf1ca931-8685-4ea7-b554-85926ec6e9e5")
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .dob(dob)
                .phoneNumber("0563016466")
                .build();
    }

    @Test
    void register_validRequest_success() {
        // GIVEN
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = authenticationService.register(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("bf1ca931-8685-4ea7-b554-85926ec6e9e5");
        Assertions.assertThat(response.getPhoneNumber()).isEqualTo("0563016466");
    }

    @Test
    void register_invalidRequest_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> authenticationService.register(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    void register_InvalidEmail_fail() {
        user.setEmail("test01@@gmail.comn");
        request.setEmail("test01@@gmail.comn");
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // WHEN
        var exception = authenticationService.register(request);

        // THEN
        Assertions.assertThat(exception.getEmail()).isEqualTo("test01@@gmail.comn").withFailMessage("Your email has already existed");

    }

    @Test
    void register_InvalidPhoneNumber_fail() {
        request.setPhoneNumber("098765234");
        user.setPhoneNumber("098765234");
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // WHEN
        var exception = authenticationService.register(request);

        // THEN
        Assertions.assertThat(exception.getPhoneNumber())
                .isEqualTo("098765234");
        Assertions.assertThat(exception.getPhoneNumber()).isNotEmpty();
    }

    @Test
    void register_InvalidDob_fail() {
        request.setDob(LocalDate.of(2020, 3, 3));
        user.setDob(LocalDate.of(2020, 3, 3));
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // WHEN
        var exception = authenticationService.register(request);

        // THEN
        Assertions.assertThat(exception.getDob())
                .isEqualTo(LocalDate.of(2020, 3, 3));
    }

    @Test
    void register_ExistedEmail_fail() {
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        // WHEN
        var exception = assertThrows(AppException.class, () -> authenticationService.register(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1010);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("Your email has already existed");
    }

    @Test
    void register_ExistedPhoneNumber_fail() {
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // WHEN
        var exception = assertThrows(AppException.class, () -> authenticationService.register(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1008);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("Your phone number has already existed");
    }

    @Test
    void register_UsernameInvalid_Fail() {
        request.setUsername("tes");
        user.setUsername("tes");
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var exception = authenticationService.register(request);

        // THEN
        Assertions.assertThat(exception.getUsername()).isEqualTo("tes").withFailMessage("Username must be at least 4 characters ");
    }

    @Test
    void register_FirstNameInvalid_Fail() {
        // GIVEN
        request = new RegisterRequest();
        request.setUsername("test04");
        request.setFirstName("Ti");
        request.setLastName("Quang");
        request.setEmail("test04@gmail.com");
        request.setPassword("Tien02122002@");
        request.setDob(LocalDate.of(2002, 3, 3));
        request.setPhoneNumber("0563016455");
        user = new User();
        user.setUsername("test04");
        user.setFirstName("Ti");
        user.setLastName("Quang");
        user.setEmail("test04@gmail.com");
        user.setPassword("Tien02122002@");
        user.setDob(LocalDate.of(2002, 3, 3));
        user.setPhoneNumber("0563016455");
        // WHEN
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        var response = authenticationService.register(request);
        //    System.out.println("Response object: " +authenticationService.register(request));
        Assertions.assertThat(response.getFirstName())
                .isEqualTo("Ti").withFailMessage("Firstname must be at least 3 characters");

    }

    @Test
    void register_LastNameInvalid_Fail() {

        // GIVEN
        request = new RegisterRequest();
        request.setUsername("test04");
        request.setFirstName("Tien");
        request.setLastName("QA");
        request.setEmail("test04@gmail.com");
        request.setPassword("Tien02122002@");
        request.setDob(LocalDate.of(2002, 3, 3));
        request.setPhoneNumber("0563016455");
        user = new User();
        user.setUsername("test04");
        user.setFirstName("Tien");
        user.setLastName("QA");
        user.setEmail("test04@gmail.com");
        user.setPassword("Tien02122002@");
        user.setDob(LocalDate.of(2002, 3, 3));
        user.setPhoneNumber("0563016455");
        // WHEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        var response = authenticationService.register(request);
        //    System.out.println("Response object: " +authenticationService.register(request));
        Assertions.assertThat(response.getLastName())
                .isEqualTo("QA");

    }

    @Test
    void Login_validRequest_Success() {
        LoginRequest request = new LoginRequest();
        request.setPhoneNumber("0987457836");
        request.setPassword("Test052002@");

        user.setPhoneNumber("0987457836");
        user.setPassword("$2a$10$BstjgU3EED/pAwv3mndbtOrQWiB2by35xF2YOgQwqJRBpFrYMjx2.");

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);
        LoginResponse response = authenticationService.login(request);
        assertNotNull(response);
        assertTrue(response.isAuthenticated());

    }

    @Test
    void login_invalidRequest_fail() {
        // GIVEN
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(java.util.Optional.of(user));

        // WHEN
        var exception = assertThrows(
                AppException.class, () -> authenticationService.login(new LoginRequest("0563016466", "Quang09122002")));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    void login_invalidRequest_fail_with_Wrong_phoneNumber() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(java.util.Optional.of(user));

        var exception = assertThrows(
                AppException.class,
                () -> authenticationService.login(new LoginRequest("0563016466", "Quang09122002@")));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    void login_invalidRequest_fail_with_WrongPassWord() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(java.util.Optional.of(user));

        var exception = assertThrows(
                AppException.class, () -> authenticationService.login(new LoginRequest("0563016466", "abc")));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    void generateToken() {
        user.setPhoneNumber("0563016466");
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        String token = authenticationService.generateToken(user);
        Assertions.assertThat(token).isNotEmpty();
        //
        // Assertions.assertThat(token).isEqualTo("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkaW5vLmNvbSIsInN1YiI6IjA1NjMwMTY0NjYiLCJleHAiOjIwMjA4NjkyOTIsImlhdCI6MTcxNjMwOTI5MiwianRpIjoiZjE3OTNlY2ItYWMzYy00Mjk0LWE5YjUtODYxY2ZhOTc2YmFmIiwic2NvcGUiOiIifQ.8He5FiB9oLViSBobn39Rr6NXj4dy65dChPHzFjjmNCD36nirxu4IvxIJzRQaOJZWkysmuTjkThTcBJWxkYLRxA");
    }

    @Test
    void refreshToken() throws ParseException, JOSEException {
        String oldToken = authenticationService.generateToken(user);

        SignedJWT signedJWT = SignedJWT.parse(oldToken);
        InvalidToken invalidToken = new InvalidToken(
                signedJWT.getJWTClaimsSet().getJWTID(),
                signedJWT.getJWTClaimsSet().getExpirationTime());

        when(invalidTokenRepository.save(any(InvalidToken.class))).thenReturn(invalidToken);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setAccessToken(oldToken);

        LoginResponse response = authenticationService.refreshToken(request);

        assertNotNull(response);
        assertTrue(response.isAuthenticated());
    }

    @Test
    void builScope() {
        Role role = new Role();
        role.setName("USER");
        user.setRoles(Set.of(role));

        String scope = authenticationService.buildScope(user);

        assertEquals("ROLE_USER", scope);
    }

    @Test
    void verifyToken() throws ParseException, JOSEException {
        String token = authenticationService.generateToken(user);

        SignedJWT verifiedToken = authenticationService.verifyToken(token, false);

        Assertions.assertThat(verifiedToken).isNotNull();
    }

    @Test
    void introspect_test() throws ParseException, JOSEException {
        String token = authenticationService.generateToken(user);

        IntrospectRequest request = new IntrospectRequest();
        request.setToken(token);

        IntrospectResponse response = authenticationService.introspect(request);

        assertTrue(response.isValid());
    }

    @Test
    void logout_Success() throws ParseException, JOSEException {
        String token = authenticationService.generateToken(user);

        LogoutRequest request = new LogoutRequest();
        request.setAccessToken(token);
        SignedJWT signedJWT = SignedJWT.parse(token);
        authenticationService.logout(request);
        InvalidToken invalidToken = new InvalidToken(
                signedJWT.getJWTClaimsSet().getJWTID(),
                signedJWT.getJWTClaimsSet().getExpirationTime());
        when(invalidTokenRepository.save(any(InvalidToken.class))).thenReturn(invalidToken);
    }

    @Test
    void forgotPassword_Success(){
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("test04@gmail.com");
        forgotPasswordRequest.setPassword("Huy09122002@");
        forgotPasswordRequest.setConfirmPassword("Huy09122002@");
        when(passwordEncoder.encode("Huy09122002@")).thenReturn("$2a$10$2vEGTnlBs43HJz82IZg6LOr.WvcedCFtZ2khnV1VrKWjP9WvzksU.");
        authenticationService.forgotPassword(forgotPasswordRequest);

        verify(userRepository).updatePassword(eq("test04@gmail.com"), anyString());
    }

    @Test
    void forgotPassword_MisMatch_Fail(){
            ForgotPasswordRequest request = new ForgotPasswordRequest();
            request.setEmail("test04@gmail.com");
            request.setPassword("Huy09122002@");
            request.setConfirmPassword("Anh09122002@");

            assertThrows(AppException.class, () -> authenticationService.forgotPassword(request));
            verify(userRepository, never()).updatePassword(anyString(), anyString());
    }

    @Test
    void forgotPassword_InvalidEmail(){
        String email = "test10@gmail.com";
        String password = "Huy09122002@";
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setConfirmPassword(password);
        authenticationService.forgotPassword(request);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        verify(userRepository, never()).updatePassword(email, password);

    }

    @Test
    void changePassword_Success() {
        // Given
        String userId = "e3b8df40-ce71-415a-a18e-deb998fba438";
        String pass = "Anh09122002@";
        String newPass = "Huy09122002@";
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(pass);
        request.setNewPassword(newPass);

        User user = new User();
        user.setId(userId);
        user.setPassword("$2a$10$j7gNK.nqjwM.mtZLeWyD1ufux0qMW.aJV2Z877dbR/VVTTkSUxLiG");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        String encodedPassword = "$2a$10$lR6bxShpSyvBdN2KbdV.puUl2bGJyWn9QJt0qGAqpw7FQQRnn3mT6";
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn(encodedPassword);

        // When
        authenticationService.changePassword(userId, request);

        verify(userRepository).changePassword(eq(userId), anyString());
        assertEquals(encodedPassword, passwordEncoder.encode(request.getNewPassword()));

    }


    @Test
    void changePassword_UserNotFound(){
        String userId = "691d6c62-b34c-4bfe-ba9f-9db3f34f83";
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("Huy09122002@");
        request.setNewPassword("Anh09122002@");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> authenticationService.changePassword(userId, request));
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
        verify(userRepository, never()).changePassword(anyString(), anyString());
    }
    @Test
    void changePassword_invalidOldPassword(){
        String userId = "691d6c62-b34c-4bfe-ba9f-9db3f34f8321";
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("Huy09122002@");
        request.setNewPassword("ANh09122002@");

        user.setId(userId);
        user.setPassword("encodedOldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Huy09122002@", "ANh09122002@")).thenReturn(false);

        assertThrows(AppException.class, () -> authenticationService.changePassword(userId, request));
        verify(userRepository, never()).changePassword(anyString(), anyString());

    }

    //    @Test
    //    @WithMockUser(
    //            username = "test02",
    //            roles = {"USER"})
    //    void getProfile_validRequest_success() {
    //        // GIVEN
    //        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
    //
    //        // WHEN
    //        var response = userService.getProfile();
    //
    //        // THEN
    //        Assertions.assertThat(response.getUsername()).isEqualTo("test02");
    //        Assertions.assertThat(response.getId()).isEqualTo("42e2-bae5-9ea7c0f1c4d4");
    //    }
    //
    //    @Test
    //    @WithMockUser(
    //            username = "test02",
    //            roles = {"USER"})
    //    void getProfile_invalidRequest_fail() {
    //        // GIVEN
    //        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.ofNullable(null));
    //
    //        // WHEN
    //        var exception = assertThrows(AppException.class, () -> userService.getProfile());
    //
    //        // THEN
    //        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    //    }

}
