package com.ugts.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    Long id;
    String street;
    String district;
    String province;
    String country;
    String addressLine;
    boolean isDefault;
}
