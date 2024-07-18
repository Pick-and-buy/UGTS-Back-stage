package com.ugts.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewAddressRequest {
    String street;
    String district;
    String province;
    String country;
    String addressLine;
}
