package com.ugts.brandLine.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ugts.brand.entity.Brand;
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
public class BrandLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Brand brand;

    @OneToMany(mappedBy = "brandLine", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Category> categories = new HashSet<>();

    String lineName;

    @Column(length = 500)
    String description;

    String launchDate;

    String signatureFeatures;

    String priceRange;

    Boolean availableStatus;

    Date createdAt;

    Date updatedAt;

    @OneToMany(mappedBy = "brandLine", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<BrandLineImage> brandLineImages = new HashSet<>();
}
