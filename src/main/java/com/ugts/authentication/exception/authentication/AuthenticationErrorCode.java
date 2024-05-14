package com.ugts.authentication.exception.authentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum AuthenticationErrorCode {
    UNAUTHENTICATED(1001, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "Unauthorized!", HttpStatus.FORBIDDEN),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
