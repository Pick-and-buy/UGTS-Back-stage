package com.ugts.homepage.service;

import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;

import java.util.List;

public interface IRecommendationService {
    List<PostResponse> getRecommendationsForUser(String userId);
}
