package com.ugts.user.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ugts.comment.entity.Comment;
import com.ugts.follow.entity.Follow;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String username;

    String avatar;

    String password;

    String lastName;

    String firstName;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String phoneNumber;

    Date dob;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Address> address = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Post> createdPosts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();

    @ManyToMany
    Set<Post> likedPosts = new HashSet<>();

    @ManyToMany
    Set<Post> viewedPosts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "purchasedUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Post> purchasedPosts = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "following")

    Set<Follow> followers = new HashSet<>();
}
