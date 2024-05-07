package com.ugts.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.ugts.dto.request.IntrospectRequest;
import com.ugts.dto.request.LoginRequest;
import com.ugts.dto.request.RefreshTokenRequest;
import com.ugts.dto.response.IntrospectResponse;
import com.ugts.dto.response.LoginResponse;
import com.ugts.entity.User;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);

    String generateToken(User user);

    LoginResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;

    String buildScope(User user);

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
