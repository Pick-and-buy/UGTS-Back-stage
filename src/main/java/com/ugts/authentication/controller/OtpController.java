package com.ugts.authentication.controller;

import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.request.VerifyOtpRequest;
import com.ugts.authentication.dto.response.ForgotPasswordResponse;
import com.ugts.authentication.service.OtpViaEmailService;
import com.ugts.authentication.service.TwilioService;
import com.ugts.common.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpController {
    OtpViaEmailService otpViaEmailService;

    TwilioService twilioService;

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

    @PostMapping("/send-sms-otp")
    public ApiResponse<ForgotPasswordResponse> sendSmsOtp(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        var result = twilioService.sendOTPBySMS(forgotPasswordRequest);
        return ApiResponse.<ForgotPasswordResponse>builder()
                .message("Send SMS Success")
                .result(result)
                .build();
    }

    @PostMapping("/verify-sms-otp")
    public ApiResponse<String> validateSmsOtp(@RequestParam Integer otp, @RequestParam String phoneNumber) {
        var result = twilioService.validateOtp(otp, phoneNumber);
        return ApiResponse.<String>builder()
                .message("Valid OTP!")
                .result(result)
                .build();
    }
}
