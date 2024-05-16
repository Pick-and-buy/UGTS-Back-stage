package com.ugts.authentication.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import com.ugts.authentication.dto.record.ViaMailBody;
import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.dto.request.VerifyOtpRequest;
import com.ugts.authentication.entity.OtpViaEmail;
import com.ugts.authentication.repository.OtpViaEmailRepository;
import com.ugts.authentication.service.OtpViaEmailService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OtpViaEmailServiceImpl implements OtpViaEmailService {
    JavaMailSender mailSender;

    UserRepository userRepository;

    OtpViaEmailRepository otpViaEmailRepository;

    Random random = new Random();

    @Override
    public void sendOtpCode(ForgotPasswordRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        int otpCode = otpGenerator();
        ViaMailBody mailBody = ViaMailBody.builder()
                .to(request.getEmail())
                .text("This is the OTP for forgot password: " + otpCode)
                .subject("OTP for Forgot Password request")
                .build();

        OtpViaEmail otpViaEmail = OtpViaEmail.builder()
                .otpCode(otpCode)
                .expireTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        sendSimpleMessage(mailBody);
        otpViaEmailRepository.save(otpViaEmail);
    }

    @Override
    public void verifyOtpCode(VerifyOtpRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var otpCode = otpViaEmailRepository
                .findByOtpAndUser(request.getOtpCode(), user)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        if (otpCode.getExpireTime().before(Date.from(Instant.now()))) {
            otpViaEmailRepository.delete(otpCode);
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        if (otpCode.getOtpCode().equals(request.getOtpCode())) {
            otpViaEmailRepository.delete(otpCode);
        }
    }

    public void sendSimpleMessage(ViaMailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailBody.to());
        message.setFrom("noreply@ugts.com");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        mailSender.send(message);
    }

    public Integer otpGenerator() {
        return random.nextInt(100000, 999999);
    }
}
