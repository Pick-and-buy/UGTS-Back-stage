package com.ugts.authentication.exception.otp;

import com.ugts.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class OtpExceptionHandler {

    @ExceptionHandler(value = OtpException.class)
    ResponseEntity<ApiResponse> handleOtpException(OtpException e) {
        OtpErrorCode errorCode = e.getOtpErrorCode();

        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
