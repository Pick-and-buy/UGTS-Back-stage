package com.ugts.controller;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.ugts.dto.request.IntrospectRequest;
import com.ugts.dto.request.LoginRequest;
import com.ugts.dto.request.RefreshTokenRequest;
import com.ugts.dto.response.ApiResponse;
import com.ugts.dto.response.IntrospectResponse;
import com.ugts.dto.response.LoginResponse;
import com.ugts.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        var result = authenticationService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/verify-token")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }
}
