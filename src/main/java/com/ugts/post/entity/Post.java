package com.ugts.post.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ugts.comment.entity.Comment;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    User purchasedUser;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();

    @ManyToMany(mappedBy = "likedPosts")
    Set<User> likedUsers = new HashSet<>();

    @ManyToMany(mappedBy = "viewedPosts")
    Set<User> viewedUsers = new HashSet<>();

    String thumbnail;

    String title;

    String description;

    Boolean isAvailable;

    @OneToOne
    Product product;

    Date createdAt;

    Date updatedAt;
}
