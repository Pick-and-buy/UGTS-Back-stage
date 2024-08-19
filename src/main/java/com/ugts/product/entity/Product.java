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

    @Column(columnDefinition = "TEXT")
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

    @Column(length = 500)
    String color;

    @Column(length = 500)
    String size;

    @Column(length = 500)
    String width;

    @Column(length = 500)
    String height;

    @Column(length = 500)
    String length;

    @Column(length = 500)
    String referenceCode;

    @Column(length = 500)
    String manufactureYear;

    @Column(length = 500)
    String exteriorMaterial;

    @Column(length = 500)
    String interiorMaterial;

    @Enumerated(EnumType.STRING)
    Condition condition;

    @Column(length = 500)
    String accessories;
    String dateCode;
    String serialNumber;

    @Column(columnDefinition = "TEXT")
    String purchasedPlace;

    @Column(columnDefinition = "TEXT")
    String story;

    @Enumerated(EnumType.STRING)
    VerifiedLevel verifiedLevel;

    @Column(columnDefinition = "TEXT")
    String productVideo;

    @Column(columnDefinition = "TEXT")
    String originalReceiptProof;
}
