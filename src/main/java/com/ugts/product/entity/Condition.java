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
public enum Condition {
    BRAND_NEW("Brand New: Never been used and show no signs of wear."),
    EXCELLENT("Excellent: Minimal use and show no signs of wear."),
    VERY_GOOD("Very Good: Very minor signs of wear."),
    GOOD("Good: Small signs of slight damage and wear."),
    FAIR("Fair: Signs of use and some damage.");

    String description;
}
