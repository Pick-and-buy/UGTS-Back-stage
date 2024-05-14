package com.ugts.authentication.exception.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpException extends RuntimeException {
    private OtpErrorCode otpErrorCode;
}
