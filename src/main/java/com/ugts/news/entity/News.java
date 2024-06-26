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

    @Column(length = 5000)
    String content;

    String subTitle1;

    @Column(length = 5000)
    String subContent1;

    String subTitle2;

    @Column(length = 5000)
    String subContent2;

    String subTitle3;

    @Column(length = 5000)
    String subContent3;

    @Column(length = 5000)
    String subContent4;

    @Column(length = 5000)
    String subContent5;

    @Column(length = 5000)
    String subContent6;

    @Column(length = 5000)
    String subContent7;

    @Column(length = 5000)
    String subContent8;

    @Column(length = 5000)
    String subContent9;

    @Column(length = 5000)
    String subContent10;
}
