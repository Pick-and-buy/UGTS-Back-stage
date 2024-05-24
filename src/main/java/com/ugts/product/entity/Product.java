package com.ugts.product.entity;

import java.util.HashSet;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<ProductImage> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    Brand brand;

    double price;
    String color;
    String size;
    String condition;
    String material;
    Boolean isVerify;
}
