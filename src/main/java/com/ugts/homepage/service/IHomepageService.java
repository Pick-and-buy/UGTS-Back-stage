package com.ugts.homepage.service;

import com.ugts.post.dto.response.PostResponse;

import java.util.List;

public interface IHomepageService {
    List<PostResponse> getRecommendationsForUser(String userId);

    List<PostResponse> getFollowedPosts(String userId);

}
