package com.ugts.authentication.service;

import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.request.VerifyOtpRequest;

public interface OtpViaEmailService {
    void sendOtpCode(ForgotPasswordRequest request);

    void verifyOtpCode(VerifyOtpRequest request);
}
