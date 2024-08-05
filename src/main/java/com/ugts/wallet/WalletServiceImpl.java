package com.ugts.wallet;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("hasRole('USER')")
    public Double showBalance(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user.getWallet().getBalance();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void fundTransfer(String fromUserId, String toUserId, Double amount) {

    }
    @Override
    @PreAuthorize("hasRole('USER')")
    public void depositMoney(String userId, Double amount) {

    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public void withdrawMoney(String userId, Double amount) {

    }
}
