package com.ugts.common.configuration;

import com.twilio.Twilio;
import com.ugts.authentication.resource.TwilioOTPHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class TwilioConfig {

    private final TwilioOTPHandler twilioOTPHandler;

    @Value("${twilio.account.sid}")
    private String sID;

    @Value("${twilio.auth.token}")
    private String authToken;

    @PostConstruct
    public void initTwilio(){
        Twilio.init(sID, authToken);
    }

    @Bean
    public RouterFunction<ServerResponse> handleSMS() {
        return RouterFunctions.route()
                .POST("/api/v1/router/sendOTP", twilioOTPHandler::sendOTP)
                .POST("/api/v1/router/validateOTP", twilioOTPHandler::validateOTP)
                .build();
    }
}
