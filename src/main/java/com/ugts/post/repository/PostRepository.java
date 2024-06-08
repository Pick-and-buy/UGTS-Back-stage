package com.ugts.post.repository;

import com.ugts.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    //TODO: use this 3 function
    @Query("SELECT p FROM Post p JOIN p.likedUsers u WHERE u.id = :userId")
    List<Post> findPostsLikedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p JOIN p.viewedUsers u WHERE u.id = :userId")
    List<Post> findPostsViewedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p JOIN p.purchasedUser u WHERE u.id = :userId")
    List<Post> findPostsPurchasedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :followedUserIds")
    List<Post> findPostsByFollowedUsers(@Param("followedUserIds") List<String> followedUserIds);
}
