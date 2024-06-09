package com.ugts.brand.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class BrandCollectionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    BrandCollection brandCollection;

    String collectionImageUrl;
}
