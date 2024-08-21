package com.ugts.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer productDataId;

    String productName;

    String brandLine;

    String brand;

    String category;

    String color;

    String size;

    String width;

    String height;

    String length;

    String exteriorMaterial;

    String interiorMaterial;

    @Column(columnDefinition = "TEXT")
    String story;
}
