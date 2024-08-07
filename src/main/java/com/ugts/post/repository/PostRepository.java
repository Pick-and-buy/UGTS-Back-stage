package com.ugts.post.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.ugts.post.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// @Repository("postRepository")
public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p JOIN p.likedUsers u WHERE u.id = :userId")
    List<Post> findPostsLikedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p JOIN p.viewedUsers u WHERE u.id = :userId")
    List<Post> findPostsViewedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p JOIN p.purchasedUser u WHERE u.id = :userId")
    List<Post> findPostsPurchasedByUser(@Param("userId") String userId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :followedUserIds")
    List<Post> findPostsByFollowedUsers(@Param("followedUserIds") List<String> followedUserIds);

    @Query("SELECT p FROM Post p WHERE p.user.id = :followedUserId")
    List<Post> findPostsByFollowedUsers(String followedUserId);

    @Query("SELECT p FROM Post p JOIN p.product pr JOIN pr.brand b WHERE b.name = :brandName")
    List<Post> findAllByBrandName(String brandName);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findPostsByUserId(String userId);

    // search with title or description
    @Query("SELECT p FROM Post p WHERE lower(p.title) LIKE lower(concat('%', :keyword, '%'))"
            + "OR lower(p.description) LIKE lower(concat('%', :keyword, '%'))")
    List<Post> findByTitleContainingKeyword(@Param("keyword") String keyword);

    // search with status by sql query
    @Query("SELECT p FROM Post p WHERE p.isAvailable = :status")
    List<Post> findByStatus(@Param("status") boolean status);

    List<Post> findPostsByIsAvailable(boolean status);

    @Query("SELECT p FROM Post p JOIN p.product pr JOIN pr.brandLine bl WHERE bl.lineName = :brandLineName")
    List<Post> findAllByBrandLine(String brandLineName);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.boosted = false, p.boostEndTime = null WHERE p.boostEndTime < ?1 AND p.boosted = true")
    void resetExpiredBoosts(LocalDateTime now);
}
