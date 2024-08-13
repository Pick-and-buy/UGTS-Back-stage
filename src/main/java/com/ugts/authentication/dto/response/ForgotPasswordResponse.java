package com.ugts.authentication.dto.response;

import com.ugts.authentication.entity.OtpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordResponse {
    private OtpStatus status;
    private String message;
}
