package com.ugts.follow.controller;

import java.util.List;

import com.ugts.dto.ApiResponse;
import com.ugts.follow.dto.FollowRequestDto;
import com.ugts.follow.service.IFollowService;
import com.ugts.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowController {
    private final IFollowService followService;

    @PostMapping("/follow")
    public ApiResponse<Void> followUser(@RequestBody FollowRequestDto request) {
        followService.followUser(request);
        return ApiResponse.<Void>builder().message("Success follow user").build();
    }

    @DeleteMapping("/unfollow")
    public ApiResponse<Void> unfollowUser(@RequestBody FollowRequestDto request) {
        followService.unfollowUser(request);
        return ApiResponse.<Void>builder().message("Success unfollow user").build();
    }

    // TODO: Implement logic
    @GetMapping("/following/{userId}")
    public ApiResponse<List<UserResponse>> getFollowing(@PathVariable String userId) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(followService.getFollowing(userId))
                .build();
    }

    @GetMapping("/followers/{userId}")
    public ApiResponse<List<UserResponse>> getFollowers(@PathVariable String userId) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(followService.getFollowers(userId))
                .build();
    }
}
