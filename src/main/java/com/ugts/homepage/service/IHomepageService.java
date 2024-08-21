package com.ugts.homepage.service;

import java.util.List;

import com.ugts.post.dto.response.PostResponse;

public interface IHomepageService {
    List<PostResponse> getRecommendationsForUser(String userId);

    List<PostResponse> getFollowedPosts(String userId);
}
