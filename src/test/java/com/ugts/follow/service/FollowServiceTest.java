package com.ugts.follow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.follow.dto.FollowRequestDto;
import com.ugts.follow.entity.Follow;
import com.ugts.follow.repository.FollowRepository;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowServiceImpl followService;

    @Mock
    private UserMapper userMapper;

    @Test
    void followUser_success() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec22147c9");
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");
        User followUser = new User();
        followUser.setId("165fb1ba-621d-4035-b8be-508ec22147c9");

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(userRepository.findById("165fb1ba-621d-4035-b8be-508ec22147c9")).thenReturn(Optional.of(followUser));
        when(followRepository.findByFollowerAndFollowing(user, followUser)).thenReturn(null);

        followService.followUser(followRequestDto);

        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void followUser_whenUserAlreadyFollowed_fail() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec22147c9");
        User user = new User();
        User followUser = new User();
        when(userRepository.findById(followRequestDto.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.findById(followRequestDto.getTargetUserId())).thenReturn(Optional.of(followUser));
        when(followRepository.findByFollowerAndFollowing(user, followUser)).thenReturn(new Follow());

        AppException exception = assertThrows(AppException.class, () -> followService.followUser(followRequestDto));

        assertEquals(ErrorCode.USER_ALREADY_FOLLOWED, exception.getErrorCode());
    }

    @Test
    void followUser_whenTargetUserDoesNotExist_fail() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec2147c");
        User user = new User();
        when(userRepository.findById(followRequestDto.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.findById(followRequestDto.getTargetUserId())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> followService.followUser(followRequestDto));

        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void followUser_whenUserNotExist_fail() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("165fb1ba-621d-4035-b8be-508ec22147c", "165fb1ba-621d-4035-b8be-508ec22147c9");
        when(userRepository.findById(followRequestDto.getUserId())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> followService.followUser(followRequestDto));

        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void testUnfollowUserSuccess() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec22147c9");
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");
        User followUser = new User();
        followUser.setId("165fb1ba-621d-4035-b8be-508ec22147c9");
        Follow follow = new Follow();
        follow.setFollower(user);
        follow.setFollowing(followUser);

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(userRepository.findById("165fb1ba-621d-4035-b8be-508ec22147c9")).thenReturn(Optional.of(followUser));
        when(followRepository.findByFollowerAndFollowing(user, followUser)).thenReturn(follow);

        followService.unfollowUser(followRequestDto);

        verify(followRepository).delete(follow);
    }

    @Test
    void unfollowUser_UserNotFound_fail() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec22147c9");

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> followService.unfollowUser(followRequestDto));
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void unfollowUser_TargetUserNotFound_fail() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec22147c9");
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(userRepository.findById("165fb1ba-621d-4035-b8be-508ec22147c9")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> followService.unfollowUser(followRequestDto));
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void unfollowUserNotFollowing_fail() {
        FollowRequestDto followRequestDto =
                new FollowRequestDto("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7", "165fb1ba-621d-4035-b8be-508ec22147c9");
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");
        User followUser = new User();
        followUser.setId("165fb1ba-621d-4035-b8be-508ec22147c9");

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(userRepository.findById("165fb1ba-621d-4035-b8be-508ec22147c9")).thenReturn(Optional.of(followUser));
        when(followRepository.findByFollowerAndFollowing(user, followUser)).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> followService.unfollowUser(followRequestDto));
        assertEquals(ErrorCode.USER_NOT_FOLLOWED, exception.getErrorCode());
    }

    @Test
    void getFollowing_Success() {
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");
        Follow follow = new Follow();
        follow.setFollower(user);
        User followedUser = new User();
        followedUser.setId("165fb1ba-621d-4035-b8be-508ec22147c9");
        follow.setFollowing(followedUser);
        UserResponse userResponse = new UserResponse();

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(followRepository.findByFollower(user)).thenReturn(List.of(follow));
        when(userMapper.userToUserResponse(followedUser)).thenReturn(userResponse);

        List<UserResponse> responses = followService.getFollowing("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(userResponse, responses.get(0));
    }

    @Test
    void getFollowing_UserNotFound_fail() {
        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.empty());

        AppException exception = assertThrows(
                AppException.class, () -> followService.getFollowing("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7"));
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void testGetFollowingEmptyList() {
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(followRepository.findByFollower(user)).thenReturn(Collections.emptyList());

        List<UserResponse> responses = followService.getFollowing("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void getFollowers_Success() {
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");
        Follow follow = new Follow();
        follow.setFollowing(user);
        User followerUser = new User();
        followerUser.setId("165fb1ba-621d-4035-b8be-508ec22147c9");
        follow.setFollower(followerUser);
        UserResponse userResponse = new UserResponse();

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(followRepository.findByFollowing(user)).thenReturn(List.of(follow));
        when(userMapper.userToUserResponse(followerUser)).thenReturn(userResponse);

        List<UserResponse> responses = followService.getFollowers("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(userResponse, responses.get(0));
    }

    @Test
    void getFollowers_UserNotFound_fail() {
        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.empty());

        AppException exception = assertThrows(
                AppException.class, () -> followService.getFollowers("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7"));
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void getFollowers_EmptyList_fail() {
        User user = new User();
        user.setId("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        when(userRepository.findById("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7")).thenReturn(Optional.of(user));
        when(followRepository.findByFollowing(user)).thenReturn(Collections.emptyList());

        List<UserResponse> responses = followService.getFollowers("1bac99aa-4bf0-479b-bd8a-19e9f2b32ce7");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}
