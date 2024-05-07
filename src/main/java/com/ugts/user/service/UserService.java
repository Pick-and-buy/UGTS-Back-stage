package com.ugts.user.service;

import java.util.List;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.user.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);
}
