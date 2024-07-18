package com.ugts.verifyid.service;

import com.ugts.verifyid.dto.VerifyInformationRequest;
import com.ugts.verifyid.entity.VerifyInformation;

public interface IVerifyInformation {
     void handleVerifyUser(VerifyInformationRequest verifyInformationRequest);
}
