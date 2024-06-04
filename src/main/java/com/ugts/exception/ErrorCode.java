package com.ugts.exception;

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
public enum ErrorCode {

    // API error
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_MESSAGE_KEY(8888, "Invalid message key", HttpStatus.BAD_REQUEST),

    // Authentication
    UNAUTHENTICATED(1001, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "Unauthorized!", HttpStatus.FORBIDDEN),

    // User
    USER_EXISTED(1001, "User has already existed!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User not exist!", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Your username must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_1(1004, "Password must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_2(
            1005,
            "Password must contain at least 1 small letter, capital letter, number and special character",
            HttpStatus.BAD_REQUEST),

    INVALID_DOB(1006, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID(1007, "Your phone number must be {min} number", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1008, "Your phone number has already existed", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1009, "Your email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1010, "Your email has already existed", HttpStatus.BAD_REQUEST),
    LAST_NAME_INVALID(1011, "Last name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    FIRST_NAME_INVALID(1012, "First name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(1013, "Password mismatch", HttpStatus.BAD_REQUEST),
    INVALID_OLD_PASSWORD(1014, "Invalid old password", HttpStatus.BAD_REQUEST),

    // OTP
    INVALID_OTP(1001, "Invalid OTP", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1002, "OTP has expired", HttpStatus.BAD_REQUEST),
    OTP_NOT_EXISTED(1003, "OTP not existed", HttpStatus.BAD_REQUEST),

    // Brand
    BRAND_EXISTED(1001, "Brand has already existed!", HttpStatus.BAD_REQUEST),
    BRAND_NOT_EXISTED(1002, "Brand not exist!", HttpStatus.BAD_REQUEST),

    // Post
    POST_NOT_FOUND(1001, "Post not found", HttpStatus.BAD_REQUEST),

    // Product
    PRODUCT_NOT_EXISTED(1001, "Product not exist", HttpStatus.BAD_REQUEST),

// Comment
;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
