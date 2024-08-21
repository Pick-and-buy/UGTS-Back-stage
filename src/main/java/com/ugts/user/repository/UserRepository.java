package com.ugts.user.repository;

import java.util.List;
import java.util.Optional;

import com.ugts.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    //    User findById(String userId);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = ?2 WHERE u.email = ?1")
    void updatePassword(String email, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = ?2 WHERE u.id = ?1")
    void changePassword(String userId, String newPassword);

    //    @Query("SELECT u FROM User u JOIN u.likedPosts p WHERE p.id = :postId")
    //    List<User> findUsersByLikedPost(@Param("postId") String postId);
    //
    //    @Query("SELECT u FROM User u JOIN u.viewedPosts p WHERE p.id = :postId")
    //    List<User> findUsersByViewedPost(@Param("postId") String postId);

    //    @Query("SELECT u FROM User u JOIN u.purchasedPosts p WHERE p.id = :postId")
    //    List<User> findUsersByPurchasedPost(@Param("postId") String postId);

    @Query("SELECT u.following FROM User u WHERE u.id = :userId")
    List<User> findFollowingUsers(@Param("userId") String userId);
}
