package com.ugts.product.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.brand.entity.Brand;
import com.ugts.brandCollection.entity.BrandCollection;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.category.entity.Category;
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
    List<ProductImage> images = new ArrayList<>();

    @JsonIgnoreProperties("hibernateLazyInitializer")
    @ManyToOne(fetch = FetchType.LAZY)
    Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    BrandLine brandLine;

    @ManyToOne(fetch = FetchType.LAZY)
    BrandCollection brandCollection;

    @JsonIgnoreProperties("hibernateLazyInitializer")
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    double price;
    String color;
    String size;
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

    @Column(length = 500)
    String story;

    @Enumerated(EnumType.STRING)
    VerifiedLevel verifiedLevel;

    String productVideo;

    String originalReceiptProof;
}
