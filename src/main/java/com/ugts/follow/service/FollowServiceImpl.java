package com.ugts.follow.service;

import java.util.List;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.follow.dto.FollowRequestDto;
import com.ugts.follow.entity.Follow;
import com.ugts.follow.repository.FollowRepository;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements IFollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final UserMapper userMapper;

    @Override
    public void followUser(FollowRequestDto followRequestDto) {
        User user = userRepository.findById(followRequestDto.getUserId()).orElseThrow();
        User followUser =
                userRepository.findById(followRequestDto.getTargetUserId()).orElseThrow();
        if (isFollowing(user, followUser)) {
            throw new AppException(ErrorCode.USER_ALREADY_FOLLOWED);
        }
        Follow follow = new Follow();
        follow.setFollower(user);
        follow.setFollowing(followUser);
        followRepository.save(follow);
        // TODO: Send notification to follow user
    }

    public boolean isFollowing(User follower, User following) {
        return followRepository.findByFollowerAndFollowing(follower, following) != null;
    }
    //    @PreAuthorize("hasRole('USER')")
    @Override
    //    @Transactional
    public void unfollowUser(FollowRequestDto followRequestDto) {
        User user = userRepository.findById(followRequestDto.getUserId()).orElseThrow();
        User following =
                userRepository.findById(followRequestDto.getTargetUserId()).orElseThrow();
        Follow follow = followRepository.findByFollowerAndFollowing(user, following);
        if (follow == null) {
            throw new AppException(ErrorCode.USER_NOT_FOLLOWED);
        }
        followRepository.delete(follow);
    }

    @Override
    public List<UserResponse> getFollowing(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Follow> follows = followRepository.findByFollower(user);
        List<User> users = follows.stream().map(Follow::getFollowing).toList();
        return users.stream().map(userMapper::userToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getFollowers(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Follow> follows = followRepository.findByFollowing(user);
        List<User> users = follows.stream().map(Follow::getFollower).toList();
        return users.stream().map(userMapper::userToUserResponse).toList();
    }
}
