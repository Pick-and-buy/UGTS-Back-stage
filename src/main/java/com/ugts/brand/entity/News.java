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

    @ManyToOne(cascade = CascadeType.ALL)
    BrandCollection brandCollection;

    @ManyToOne(cascade = CascadeType.ALL)
    BrandLine brandLine;

    String title;

    @Column(length = 500)
    String content;

    Date createdAt;

    Date updatedAt;
}
