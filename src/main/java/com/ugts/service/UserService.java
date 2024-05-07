package com.ugts.service;

import java.util.List;

import com.ugts.dto.request.RegisterRequest;
import com.ugts.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);
}
