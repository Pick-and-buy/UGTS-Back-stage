package com.ugts.authentication.controller;

import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.request.VerifyOtpRequest;
import com.ugts.authentication.service.OtpViaEmailService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpController {
    OtpViaEmailService otpViaEmailService;

    @PostMapping("/send")
    public ApiResponse<String> sendOtp(@RequestBody ForgotPasswordRequest request) {
        otpViaEmailService.sendOtpCode(request);
        return ApiResponse.<String>builder().message("OTP sent successfully").build();
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        otpViaEmailService.verifyOtpCode(request);
        return ApiResponse.<String>builder()
                .message("OTP verified successfully")
                .build();
    }
}
