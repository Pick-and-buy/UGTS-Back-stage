package com.ugts.post.entity;

import java.util.Date;

import com.ugts.product.entity.Product;
import com.ugts.user.entity.User;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    User user;

    String title;
    String description;
    Boolean isAvailable;

    @OneToOne(cascade = CascadeType.ALL)
    Product product;

    Date createdAt;
    Date updatedAt;
}
