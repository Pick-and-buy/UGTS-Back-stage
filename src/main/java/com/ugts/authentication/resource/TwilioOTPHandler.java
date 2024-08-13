package com.ugts.authentication.resource;

import com.ugts.authentication.dto.request.ForgotPasswordRequest;
import com.ugts.authentication.service.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TwilioOTPHandler {

    private final TwilioService twilioService;

    public Mono<ServerResponse> sendOTP(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ForgotPasswordRequest.class)
                .flatMap(twilioService::sendOTPBySMS)
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .body(BodyInserters.fromValue(dto)));
    }

    public Mono<ServerResponse> validateOTP(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ForgotPasswordRequest.class)
                .flatMap(dto -> twilioService.validateOtp(dto.getOtp(), dto.getPhoneNumber()))
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(dto));
    }
}
