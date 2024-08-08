package com.ugts.wallet.mapper;

import com.ugts.wallet.dto.WalletResponse;
import com.ugts.wallet.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponse walletToWalletResponse(Wallet wallet);
}
