package com.ugts.user.controller;

import java.io.IOException;
import java.util.List;

import com.ugts.dto.ApiResponse;
import com.ugts.user.dto.request.UpdateAddressRequest;
import com.ugts.user.dto.request.UserUpdateRequest;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .message("Success")
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getProfile() {
        return ApiResponse.<UserResponse>builder()
                .message("Success")
                .result(userService.getProfile())
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUserInfo(
            @PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("Update Success")
                .result(userService.updateUserInfo(userId, request))
                .build();
    }

    @PutMapping("/{userId}/avatar")
    public ApiResponse<UserResponse> updateUserAvatar(
            @PathVariable String userId, @RequestPart("avatar") MultipartFile avatar) throws IOException {
        return ApiResponse.<UserResponse>builder()
                .message("Update Avatar Success")
                .result(userService.updateUserAvatar(userId, avatar))
                .build();
    }

    @PutMapping("/address")
    public ApiResponse<UserResponse> updateAddress(
            @RequestParam String userId, @RequestBody UpdateAddressRequest request) {
        var result = userService.updateUserAddress(userId, request);
        return ApiResponse.<UserResponse>builder()
                .message("Update Address Success")
                .result(result)
                .build();
    }
}
