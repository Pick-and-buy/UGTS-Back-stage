package com.ugts.brand.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Brand brand;

    String collectionName;

    String season;

    String year;

    String theme;

    String releaseDate;

    String endDate;

    String designer;

    String description;

    Date createdAt;

    Date updatedAt;

    @OneToMany(mappedBy = "brandCollection", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<BrandCollectionImage> brandCollectionImages = new HashSet<>();
}
