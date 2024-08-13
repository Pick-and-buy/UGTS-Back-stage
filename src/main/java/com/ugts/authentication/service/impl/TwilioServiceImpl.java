package com.ugts.authentication.service.impl;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.response.ForgotPasswordResponse;
import com.ugts.authentication.entity.OtpStatus;
import com.ugts.authentication.helper.ForgotPasswordHelper;
import com.ugts.authentication.service.TwilioService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TwilioServiceImpl implements TwilioService {
    ForgotPasswordHelper forgotPasswordHelper;

    @NonFinal
    @Value("${twilio.host.numberPhone}")
    private String numberPhone;

    Map<String, Integer> otpMap = new HashMap<>();

    @Override
    public Mono<ForgotPasswordResponse> sendOTPBySMS(ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse forgotPasswordResponse = null;

        try {
            PhoneNumber to = new PhoneNumber(forgotPasswordRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(numberPhone);
            Integer otp = forgotPasswordHelper.otpGenerator();
            String otpMessage = "Dear " + to + ", Your OTP is ##" + otp + "##. Use this code to confirm forgot password. Thank you!";

            Message message = Message
                    .creator(to, from, otpMessage)
                    .create();

            otpMap.put(forgotPasswordRequest.getPhoneNumber(), otp);

            forgotPasswordResponse = new ForgotPasswordResponse(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception e) {
            forgotPasswordResponse = new ForgotPasswordResponse(OtpStatus.FAILED, e.getMessage());
        }
        return Mono.just(forgotPasswordResponse);
    }

    @Override
    public Mono<String> validateOtp(Integer otp, String phoneNumber) {
        if (otp.equals(otpMap.get(phoneNumber))){
            return Mono.just("Valid OTP!");
        }
        return Mono.error(new IllegalArgumentException("Invalid OTP! Please retry."));
    }
}
