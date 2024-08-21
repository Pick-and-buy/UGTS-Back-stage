package com.ugts.verifyid.service;

import com.ugts.verifyid.dto.VerifyInformationRequest;

public interface IVerifyInformation {
    void handleVerifyUser(VerifyInformationRequest verifyInformationRequest);
}
