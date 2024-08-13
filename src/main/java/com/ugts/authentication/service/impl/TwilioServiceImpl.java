package com.ugts.authentication.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.request.VerifyOtpRequest;
import com.ugts.authentication.dto.response.ForgotPasswordResponse;
import com.ugts.authentication.entity.OtpStatus;
import com.ugts.authentication.helper.ForgotPasswordHelper;
import com.ugts.authentication.service.TwilioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioServiceImpl implements TwilioService {
    private static final Logger logger = LoggerFactory.getLogger(TwilioServiceImpl.class);

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
            String otpMessage =
                    "Dear " + to + ", Your OTP is #" + otp + ". Use this code to confirm forgot password. Thank you!";

            Message message = Message.creator(to, from, otpMessage).create();

            otpMap.put(forgotPasswordRequest.getPhoneNumber(), otp);

            return new ForgotPasswordResponse(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception e) {
            logger.error("Error sending OTP by SMS: {}", e.getMessage());
            return new ForgotPasswordResponse(OtpStatus.FAILED, e.getMessage());
        }
    }

    @Override
    public String validateOtp(VerifyOtpRequest verifyOtpRequest) {
        Integer storedOtp = otpMap.get(verifyOtpRequest.getPhoneNumber());
        logger.info("Validating OTP for phone number: {}", verifyOtpRequest.getPhoneNumber());
        logger.info("Contents of otpMap: {}", otpMap);

        if (verifyOtpRequest.getOtpCode().equals(storedOtp)) {
            otpMap.remove(verifyOtpRequest.getPhoneNumber());
            return "Valid SMS OTP";
        } else {
            logger.error("Invalid OTP validation for phone number: {}", verifyOtpRequest.getPhoneNumber());
            throw new IllegalArgumentException("Invalid OTP! Please retry.");
        }
    }
}
