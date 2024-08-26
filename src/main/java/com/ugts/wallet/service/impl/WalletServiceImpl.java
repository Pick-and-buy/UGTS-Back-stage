package com.ugts.wallet.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.ugts.bankaccount.entity.BankAccount;
import com.ugts.bankaccount.repository.BankAccountRepository;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.order.repository.OrderRepository;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.entity.Transaction;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.transaction.enums.TransactionType;
import com.ugts.transaction.mapper.TransactionMapper;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
import com.ugts.vnpay.configuration.VnPayConfiguration;
import com.ugts.wallet.dto.WalletResponse;
import com.ugts.wallet.entity.Wallet;
import com.ugts.wallet.mapper.WalletMapper;
import com.ugts.wallet.repository.WalletRepository;
import com.ugts.wallet.service.IWalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletServiceImpl implements IWalletService {
    UserRepository userRepository;
    BankAccountRepository bankAccountRepository;
    WalletRepository walletRepository;
    TransactionRepository transactionRepository;
    UserService userService;
    WalletMapper walletMapper;
    OrderRepository orderRepository;
    TransactionMapper transactionMapper;

    /**
     * Retrieves the user by getting the user ID from the user service profile,
     * then finds and returns the user from the user repository based on the user ID.
     * Throws an 'AppException' with the error code 'USER_NOT_EXISTED' if the user is not found.
     *
     * @return The user retrieved from the repository.
     */
    private User retrieveUser() {
        var userId = userService.getProfile().getId();
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    /**
     * Charges the specified amount to the user's wallet.
     *
     * @param walletId The ID of the wallet to charge.
     * @param amount The amount to charge to the wallet.
     * @return The new balance after charging the amount to the wallet.
     * @throws AppException if the wallet is not found or if an error occurs during the charge process.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public double charge(String walletId, double amount) {
        try {
            var user = retrieveUser();

            var wallet =
                    walletRepository.findById(walletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            var currentBalance = user.getWallet().getBalance();
            double newBalance = currentBalance + amount;
            wallet.setBalance(newBalance);

            var transaction = Transaction.builder()
                    .cardType("Wallet")
                    .amount(amount)
                    .currency("VND")
                    .reason("Charge the money")
                    .createDate(LocalDateTime.now())
                    .transactionStatus(TransactionStatus.SUCCESS)
                    .user(user)
                    .wallet(wallet)
                    .transactionType(TransactionType.DEPOSIT_TO_WALLET)
                    .build();
            transactionRepository.save(transaction);

            return newBalance;
        } catch (RuntimeException e) {
            throw new RuntimeException("some thing wrong when try to charge into wallet", e);
        }
    }

    /**
     * Retrieves and returns the wallet information based on the provided wallet ID.
     *
     * @param walletId The ID of the wallet to retrieve information for.
     * @return The wallet response containing the user, wallet ID, and balance.
     * @throws AppException if the wallet with the specified ID is not found.
     */
    @Override
    @PreAuthorize("hasRole('USER')")
    public WalletResponse getWalletInformation(String walletId) {
        var wallet =
                walletRepository.findById(walletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        return walletMapper.walletToWalletResponse(wallet);
    }

    /**
     * Processes a payment for an order using the specified wallet.
     *
     * @param walletId The ID of the wallet to make the payment from.
     * @param orderId The ID of the order to pay for.
     * @param payAmount The amount to be paid for the order.
     * @return The new balance after deducting the payment amount from the wallet.
     * @throws AppException if the wallet is not found, or if there is insufficient balance for the payment.
     */
    @Override
    @PreAuthorize("hasRole('USER')")
    public double payForOrder(String walletId, String orderId, double payAmount) {
        try {
            var user = retrieveUser();

            var wallet =
                    walletRepository.findById(walletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            var currentBalance = user.getWallet().getBalance();
            if (currentBalance < payAmount) {
                throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
            }
            var newBalance = currentBalance - payAmount;
            wallet.setBalance(newBalance);

            var transNo = VnPayConfiguration.getRandomNumber(8);
            var billNo = VnPayConfiguration.getRandomNumber(8);
            var order =
                    orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            order.getOrderDetails().setIsPaid(true);

            var transaction = Transaction.builder()
                    .transNo(transNo)
                    .billNo(billNo)
                    .cardType("Wallet")
                    .amount(payAmount)
                    .currency("VND")
                    .reason("Pay For Order")
                    .createDate(LocalDateTime.now().toLocalDate())
                    .transactionStatus(TransactionStatus.SUCCESS)
                    .user(user)
                    .order(order)
                    .wallet(wallet)
                    .transactionType(TransactionType.PAY_ORDER)
                    .build();

            transactionRepository.save(transaction);

            return newBalance;
        } catch (RuntimeException e) {
            throw new RuntimeException("some thing wrong when try to pay for order", e);
        }
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<TransactionResponse> getTransactionHistories() {
        var wallet = retrieveUser().getWallet();
        var transactions = wallet.getTransactions();
        return transactionMapper.toTransactionsResponse(transactions);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void fundTransfer(String fromUserId, String toUserId, Double amount) {}

    // Rut tien vao bank
    @Override
    @PreAuthorize("hasRole('USER')")
    public void withdrawMoney(String userId, Double amount) {
        User withdrawUser =
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Wallet wallet = withdrawUser.getWallet();
        BankAccount bankAccount = bankAccountRepository.findByWalletId(wallet.getWalletId());
        if (bankAccount == null) {
            throw new AppException(ErrorCode.BANK_ACCOUNT_NOT_EXISTED);
        }
        if (wallet.getBalance() < amount) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        try {
            wallet.setBalance(wallet.getBalance() - amount);
            bankAccount.setBankBalance(bankAccount.getBankBalance() + amount);
            bankAccountRepository.save(bankAccount);
            walletRepository.save(wallet);
        } catch (Exception e) {
            throw new AppException(ErrorCode.WITHDRAW_FAIL);
        }

        // Transaction detail
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAW_TO_BANK);
        transaction.setCreateDate(LocalDateTime.now().toLocalDate());
        transaction.setAmount(amount);
        transaction.setWallet(wallet);
        transaction.setUser(withdrawUser);
        wallet.getTransactions().add(transaction);
        transactionRepository.save(transaction);
    }
}
