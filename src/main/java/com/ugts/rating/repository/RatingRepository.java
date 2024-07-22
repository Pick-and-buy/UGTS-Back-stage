package com.ugts.rating.repository;

import java.util.List;

import com.ugts.rating.entity.Rating;
import com.ugts.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findByRatedUser(User ratedUser);

    List<Rating> findByRatingUser(User ratingUser);
}
