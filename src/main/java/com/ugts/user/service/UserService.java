package com.ugts.user.service;

import java.util.List;

import com.ugts.user.dto.response.UserResponse;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);

    UserResponse getProfile();
}
