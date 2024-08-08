package com.ugts.wallet.dto;

import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {
    GeneralUserInformationDto user;
    String walletId;
    Double balance;
}
