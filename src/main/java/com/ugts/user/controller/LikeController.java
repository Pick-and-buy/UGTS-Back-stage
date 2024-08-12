package com.ugts.user.controller;

import java.util.List;

import com.ugts.common.dto.ApiResponse;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.user.dto.request.LikeRequestDto;
import com.ugts.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<Void> likePost(@RequestBody LikeRequestDto likeRequestDto) {
        try {
            userService.likePost(likeRequestDto);
            return ApiResponse.<Void>builder().message("Success like post").build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .message("Something wrong with like post")
                    .build();
        }
    }

    @DeleteMapping
    public ApiResponse<Void> unlikePost(@RequestBody LikeRequestDto likeRequestDto) {
        try {
            userService.unlikePost(likeRequestDto);
            return ApiResponse.<Void>builder().message("Success unlike post").build();
        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .message("Something wrong with unlike post")
                    .build();
        }
    }

    @GetMapping("/liked-posts/{userId}")
    public ApiResponse<List<PostResponse>> getLikedPosts(@PathVariable String userId) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(userService.getLikedPosts(userId))
                .message("Success get liked post")
                .build();
    }
}
