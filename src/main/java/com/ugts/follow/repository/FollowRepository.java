package com.ugts.follow.repository;

import java.util.List;

import com.ugts.follow.entity.Follow;
import com.ugts.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, String> {
    List<Follow> findAllById(String userId);

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowing(User following);

    Follow findByFollowerAndFollowing(User follower, User following);
}
