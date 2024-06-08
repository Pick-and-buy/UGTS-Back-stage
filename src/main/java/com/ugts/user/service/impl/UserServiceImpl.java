package com.ugts.user.service.impl;

import java.util.List;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.dto.request.LikeRequestDto;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    @PreAuthorize("hasRole('ADMIN')") // verify that the user is ADMIN before getAllUsers() is called
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    @Override
    public UserResponse getUserById(String userId) {
        return userMapper.userToUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Override
    public UserResponse getProfile() {
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.userToUserResponse(user);
    }

    public void likePost(LikeRequestDto likeRequestDto) {
        User user = userRepository.findById(likeRequestDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository.findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (user.getLikedPosts().contains(post)) {
            throw new AppException(ErrorCode.POST_ALREADY_LIKED);
        }
        user.getLikedPosts().add(post);
        userRepository.save(user);
    }

    @Override
    public void unlikePost(LikeRequestDto likeRequestDto) {
        User user = userRepository.findById(likeRequestDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository.findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (!user.getLikedPosts().contains(post)) {
            throw new AppException(ErrorCode.POST_ALREADY_UNLIKED);
        }
        user.getLikedPosts().remove(post);
        userRepository.save(user);

    }

    @Override
    public List<PostResponse> getLikedPosts(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Post> likedPosts = postRepository.findPostsLikedByUser(user.getId());
        return likedPosts.stream().map(postMapper::postToPostResponse).toList();
    }
}
