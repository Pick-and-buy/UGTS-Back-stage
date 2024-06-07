package com.ugts.post.repository;

import java.util.List;

import com.ugts.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, String> {

    @Query("SELECT p FROM Post p JOIN p.product pr JOIN pr.brand b WHERE b.name = :brandName")
    List<Post> findAllByBrandName(String brandName);
}
