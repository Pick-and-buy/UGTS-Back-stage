package com.ugts.bankaccount;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/bank-account")
@RequiredArgsConstructor
public class BankAccountController {
    private final IBankAccountService bankAccountService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> addBankAccountToWallet(
            @RequestBody BankAccountRequest bankAccountRequest, @PathVariable("id") String uniqueId) {
        bankAccountService.addBankAccount(bankAccountRequest, uniqueId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
