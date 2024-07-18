package com.ugts.user.service;

import java.io.IOException;
import java.util.List;

import com.ugts.post.dto.response.PostResponse;
import com.ugts.user.dto.request.CreateNewAddressRequest;
import com.ugts.user.dto.request.LikeRequestDto;
import com.ugts.user.dto.request.UpdateAddressRequest;
import com.ugts.user.dto.request.UserUpdateRequest;
import com.ugts.user.dto.response.AddressResponse;
import com.ugts.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);

    UserResponse getProfile();

    void likePost(LikeRequestDto likeRequestDto);

    void unlikePost(LikeRequestDto likeRequestDto);

    List<PostResponse> getLikedPosts(String userId);

    UserResponse updateUserInfo(String userId, UserUpdateRequest request);

    UserResponse updateUserAvatar(String userId, MultipartFile file) throws IOException;

    AddressResponse createNewAddress(String userId, CreateNewAddressRequest createNewAddressRequest);

    AddressResponse updateUserAddress(String userId, Long addressId, UpdateAddressRequest updateAddressRequest);

    AddressResponse setDefaultAddress(String userId, Long addressId);

    void deleteAddress(Long addressId);
}
