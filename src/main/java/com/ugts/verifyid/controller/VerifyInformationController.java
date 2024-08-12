package com.ugts.verifyid.controller;

import com.ugts.common.dto.ApiResponse;
import com.ugts.verifyid.dto.VerifyInformationRequest;
import com.ugts.verifyid.service.IVerifyInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/verify-information")
@RequiredArgsConstructor
public class VerifyInformationController {
    private final IVerifyInformation verifyInformation;

    @PostMapping
    public ApiResponse<Void> verifyInformation(@RequestBody VerifyInformationRequest verifyInformationRequest) {
        try {
            verifyInformation.handleVerifyUser(verifyInformationRequest);
            return ApiResponse.<Void>builder().message("Success verify user").build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .message("Something wrong with verify information")
                    .build();
        }
    }
}
