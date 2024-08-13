package com.ugts.authentication.service;

import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.response.ForgotPasswordResponse;

public interface TwilioService {
    ForgotPasswordResponse sendOTPBySMS(ForgotPasswordRequest forgotPasswordRequest);

    String validateOtp(Integer otp, String phoneNumber);
}
