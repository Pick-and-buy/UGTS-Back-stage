package com.ugts.homepage.controller;

import java.util.List;

import com.ugts.common.dto.ApiResponse;
import com.ugts.homepage.service.IHomepageService;
import com.ugts.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/homepage")
public class HomeController {
    private final IHomepageService homepageService;

    @GetMapping("/recommendation/{userId}")
    public ApiResponse<List<PostResponse>> recommendationList(@PathVariable String userId) {
        var recommendations = homepageService.getRecommendationsForUser(userId);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get recommendation list success")
                .result(recommendations)
                .build();
    }

    @GetMapping("/followed-posts/{userId}")
    public ApiResponse<List<PostResponse>> followedPostList(@PathVariable String userId) {
        var followerPostList = homepageService.getFollowedPosts(userId);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get followed posts success")
                .result(followerPostList)
                .build();
    }
}
