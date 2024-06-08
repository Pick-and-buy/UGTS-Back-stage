package com.ugts.user.service;

import java.io.IOException;
import java.util.List;

import com.ugts.user.dto.request.UserUpdateRequest;
import com.ugts.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);

    UserResponse getProfile();

    UserResponse updateUserInfo(String userId, UserUpdateRequest request);

    UserResponse updateUserAvatar(String userId, MultipartFile file) throws IOException;
}
