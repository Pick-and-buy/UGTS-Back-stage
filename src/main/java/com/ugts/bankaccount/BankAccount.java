package com.ugts.bankaccount;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String bankAccountId;

    @NotNull
    private String bankName;

    private Integer bankId;

    @NotNull
    private String bankBin;

    private String bankShortName;

    private String bankCode;

    private String walletId;

    @NotNull
    private Double bankBalance;


}
