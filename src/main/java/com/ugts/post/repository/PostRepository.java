package com.ugts.post.repository;

import com.ugts.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p JOIN p.likedByUsers u WHERE u.id = :userId")
    List<Post> findPostsLikedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p JOIN p.viewedByUsers u WHERE u.id = :userId")
    List<Post> findPostsViewedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p JOIN p.purchasedUser u WHERE u.id = :userId")
    List<Post> findPostsPurchasedByUser(@Param("userId") String userId);
}
