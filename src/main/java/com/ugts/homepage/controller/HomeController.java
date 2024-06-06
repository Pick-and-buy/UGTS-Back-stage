package com.ugts.homepage.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.homepage.service.impl.HomepageServiceImpl;
import com.ugts.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/homepage")
public class HomeController {
    private final HomepageServiceImpl homepageService;

    @GetMapping("/recommendation/{userId}")
    public ApiResponse<List<PostResponse>> recommendationList(@PathVariable String userId) {
        var recommendations = homepageService.getRecommendationsForUser(userId);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get recommendation list success")
                .result(recommendations)
                .build();
    }

    @GetMapping("/followed-post-list/{userId}")
    public ApiResponse<List<PostResponse>> followedPostList(@PathVariable String userId) {
        var followerPostList = homepageService.getFollowedPosts(userId);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get followed posts success")
                .result(followerPostList)
                .build();
    }
}
