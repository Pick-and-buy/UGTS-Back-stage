package com.ugts.authentication.helper;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ForgotPasswordHelper {

    Random random = new Random();

    public Integer otpGenerator() {
        return random.nextInt(100000, 999999);
    }

}
