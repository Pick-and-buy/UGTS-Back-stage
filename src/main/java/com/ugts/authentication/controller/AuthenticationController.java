package com.ugts.authentication.controller;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.ugts.authentication.dto.request.*;
import com.ugts.authentication.dto.response.IntrospectResponse;
import com.ugts.authentication.dto.response.LoginResponse;
import com.ugts.authentication.service.AuthenticationService;
import com.ugts.common.dto.ApiResponse;
import com.ugts.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody @Valid RegisterRequest request) {

        return ApiResponse.<UserResponse>builder()
                .message("Create Success")
                .result(authenticationService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var result = authenticationService.login(request);

        return ApiResponse.<LoginResponse>builder().result(result).build();
    }

    @PostMapping("/verify-token")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<LoginResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authenticationService.forgotPassword(request);
        return ApiResponse.<String>builder()
                .message("Password has been reset successfully")
                .build();
    }

    @PostMapping("/change-password/{userId}")
    public ApiResponse<String> changePassword(
            @PathVariable String userId, @Valid @RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(userId, request);
        return ApiResponse.<String>builder()
                .message("Password has been change successfully")
                .build();
    }
}
