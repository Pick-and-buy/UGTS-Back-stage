package com.ugts.authentication.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.response.ForgotPasswordResponse;
import com.ugts.authentication.entity.OtpStatus;
import com.ugts.authentication.helper.ForgotPasswordHelper;
import com.ugts.authentication.service.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioServiceImpl implements TwilioService {
    private final ForgotPasswordHelper forgotPasswordHelper;

    @Value("${twilio.host.numberPhone}")
    private String numberPhone;

    private final Map<String, Integer> otpMap = new HashMap<>();

    @Override
    public ForgotPasswordResponse sendOTPBySMS(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            PhoneNumber to = new PhoneNumber(forgotPasswordRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(numberPhone);
            Integer otp = forgotPasswordHelper.otpGenerator();
            String otpMessage = "Dear " + to + ", Your OTP is ##" + otp
                    + "##. Use this code to confirm forgot password. Thank you!";

            Message message = Message.creator(to, from, otpMessage).create();

            otpMap.put(forgotPasswordRequest.getPhoneNumber(), otp);

            return new ForgotPasswordResponse(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception e) {
            return new ForgotPasswordResponse(OtpStatus.FAILED, e.getMessage());
        }
    }

    @Override
    public String validateOtp(Integer otp, String phoneNumber) {
        if (otp.equals(otpMap.get(phoneNumber))) {
            return "Validate Success";
        }
        throw new IllegalArgumentException("Invalid OTP! Please retry.");
    }
}
