package com.ugts.homepage.service.impl;

import com.ugts.homepage.service.IRecommendationService;
import com.ugts.homepage.service.SlopeOneRecommender;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements IRecommendationService {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private SlopeOneRecommender slopeOneRecommender;
    private PostMapper postMapper;
    @Override
    public List<PostResponse> getRecommendationsForUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return  postMapper.getRecommendedPosts(slopeOneRecommender.recommend(user));
    }
}
