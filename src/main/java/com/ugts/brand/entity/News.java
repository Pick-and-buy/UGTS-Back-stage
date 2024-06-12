package com.ugts.brand.entity;

import java.util.Date;

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
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne(cascade = CascadeType.ALL)
    BrandCollection brandCollection;

    @OneToOne(cascade = CascadeType.ALL)
    BrandLine brandLine;

    String title;

    String content;

    Date createdAt;

    Date updatedAt;
}
