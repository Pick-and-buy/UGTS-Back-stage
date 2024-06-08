package com.ugts.follow.service;

import com.ugts.follow.dto.FollowRequestDto;
import com.ugts.user.dto.response.UserResponse;


import java.util.List;

public interface IFollowService {
    void followUser(FollowRequestDto requestDto);

    void unfollowUser(FollowRequestDto requestDto);

    List<UserResponse> getFollowing(String userId);

    List<UserResponse> getFollowers(String userId);
}
