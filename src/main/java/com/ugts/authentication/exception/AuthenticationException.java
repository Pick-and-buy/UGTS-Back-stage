package com.ugts.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationException extends RuntimeException {
    private AuthenticationErrorCode authenticationErrorCode;
}
