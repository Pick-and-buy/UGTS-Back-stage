package com.ugts.homepage.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ugts.homepage.service.IHomepageService;
import com.ugts.homepage.service.SlopeOneRecommender;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomepageServiceImpl implements IHomepageService {
    private UserRepository userRepository;
    private PostRepository postRepository;
    private SlopeOneRecommender slopeOneRecommender;
    private PostMapper postMapper;

    @Override
    public List<PostResponse> getRecommendationsForUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return postMapper.getRecommendedPosts(slopeOneRecommender.recommend(user));
    }

    @Override
    public List<PostResponse> getFollowedPosts(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // find followed user list
        List<User> followedUsers = userRepository.findFollowingUsers(userId);
        // followed user id list
        List<String> followedUserIds = new ArrayList<>();
        for (User followedUser : followedUsers) {
            followedUserIds.add(followedUser.getId());
        }
        List<Post> followedPosts = postRepository.findPostsByFollowedUsers(followedUserIds);
        return postMapper.getFollowedPosts(followedPosts);
    }
}
