package com.ugts.follow.service;

import java.util.List;

import com.ugts.follow.dto.FollowRequestDto;
import com.ugts.user.dto.response.UserResponse;

public interface IFollowService {
    void followUser(FollowRequestDto requestDto);

    void unfollowUser(FollowRequestDto requestDto);

    List<UserResponse> getFollowing(String userId);

    List<UserResponse> getFollowers(String userId);
}
