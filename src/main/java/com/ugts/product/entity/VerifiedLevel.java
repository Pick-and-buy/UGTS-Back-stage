package com.ugts.product.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum VerifiedLevel {
    LEVEL_1("No providing video, receipt or proof that prove the product is authentic."),
    LEVEL_2("Provided video, receipt or proof that prove the product is authentic."),
    LEVEL_3("Using our service: legit grails to check authentic product.");

    String description;
}
