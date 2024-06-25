package com.ugts.brandLine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralBrandLineInformationDto {
    Long id;
    String lineName;
    String description;
    String launchDate;
    String signatureFeatures;
    String priceRange;
    Boolean availableStatus;
}
