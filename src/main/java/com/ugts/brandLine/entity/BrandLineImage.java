package com.ugts.brandLine.entity;

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
public class BrandLineImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    BrandLine brandLine;

    String lineImageUrl;
}
