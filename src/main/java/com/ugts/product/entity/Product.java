package com.ugts.product.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandCollection;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
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

    String thumbnail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<ProductImage> images = new HashSet<>();

    @JsonIgnoreProperties("hibernateLazyInitializer")
    @ManyToOne(fetch = FetchType.LAZY)
    Brand brand;

    @ManyToOne(cascade = CascadeType.ALL)
    BrandLine brandLine;

    @ManyToOne(cascade = CascadeType.ALL)
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
    String drop;
    String fit;
    String referenceCode;
    String manufactureYear;
    String exteriorMaterial;
    String interiorMaterial;
    String condition;
    String accessories;
    String dateCode;
    String serialNumber;
    String purchasedPlace;

    @Column(length = 500)
    String story;

    Boolean isVerify;
}
