package com.ugts.brand.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Story {
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
