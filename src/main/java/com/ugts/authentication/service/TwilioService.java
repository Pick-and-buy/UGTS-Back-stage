package com.ugts.authentication.service;

import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.response.ForgotPasswordResponse;
import reactor.core.publisher.Mono;

public interface TwilioService {
    Mono<ForgotPasswordResponse> sendOTPBySMS(ForgotPasswordRequest forgotPasswordRequest);

    Mono<String> validateOtp(Integer otp, String phoneNumber);
}
