package com.ugts.authentication.helper;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordHelper {

    Random random = new Random();

    public Integer otpGenerator() {
        return random.nextInt(100000, 999999);
    }
}
