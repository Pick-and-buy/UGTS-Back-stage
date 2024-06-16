package com.ugts.product.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.brand.entity.Brand;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<ProductImage> images = new HashSet<>();

    @JsonIgnoreProperties("hibernateLazyInitializer")
    @ManyToOne(fetch = FetchType.LAZY)
    Brand brand;

    double price;
    String color;
    String size;
    String material;
    String width;
    String height;
    String length;
    String referenceCode;
    String manufactureYear;
    String exteriorMaterial;
    String interiorMaterial;

    @Enumerated(EnumType.STRING)
    Condition condition;

    String accessories;
    String dateCode;
    String serialNumber;
    String purchasedPlace;
    Boolean isVerify;
}
