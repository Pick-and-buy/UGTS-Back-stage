package com.ugts.user.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ugts.comment.entity.Comment;
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

    String username;
    String password;
    String lastName;
    String firstName;
    String email;
    String phoneNumber;
    LocalDate dob;

    @ManyToMany
    Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    Address address;

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

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id"))
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    public void addViewedPost(Post post) {
        viewedPosts.add(post);
    }

    public void addLikedPost(Post post) {
        likedPosts.add(post);
    }

    public void addPurchasedPost(Post post) {
        purchasedPosts.add(post);
    }

    public void followUser(User user) {
        followers.add(user);
        user.getFollowers().add(this);
    }

    public void unfollowUser(User user) {
        following.remove(user);
        user.getFollowers().remove(this);
    }

}
