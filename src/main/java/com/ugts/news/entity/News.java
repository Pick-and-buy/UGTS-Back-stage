package com.ugts.news.entity;

import java.util.Date;

import com.ugts.brandCollection.entity.BrandCollection;
import com.ugts.brandLine.entity.BrandLine;
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

    @ManyToOne(fetch = FetchType.LAZY)
    BrandCollection brandCollection;

    @ManyToOne(fetch = FetchType.LAZY)
    BrandLine brandLine;

    String banner;

    String title;

    Date createdAt;

    Date updatedAt;

    @Column(columnDefinition = "TEXT")
    String content;

    String subTitle1;

    @Column(columnDefinition = "TEXT")
    String subContent1;

    String subTitle2;

    @Column(columnDefinition = "TEXT")
    String subContent2;

    String subTitle3;

    @Column(columnDefinition = "TEXT")
    String subContent3;

    @Column(columnDefinition = "TEXT")
    String subContent4;

    @Column(columnDefinition = "TEXT")
    String subContent5;

    @Column(columnDefinition = "TEXT")
    String subContent6;

    @Column(columnDefinition = "TEXT")
    String subContent7;

    @Column(columnDefinition = "TEXT")
    String subContent8;

    @Column(columnDefinition = "TEXT")
    String subContent9;

    @Column(columnDefinition = "TEXT")
    String subContent10;
}
