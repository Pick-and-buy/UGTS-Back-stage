package com.ugts.verifyid.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyInformationRequest {
    String userId;
    String cardId;
    String name;
    String dob;
    String nationality;
    String home;
    String address;
    String doe;
    String features;
    String issueDate;
    String issueLoc;
    Boolean isMatch;
}
