package com.ugts.user.controller;

import java.io.IOException;
import java.util.List;

import com.ugts.dto.ApiResponse;
import com.ugts.user.dto.request.CreateNewAddressRequest;
import com.ugts.user.dto.request.UpdateAddressRequest;
import com.ugts.user.dto.request.UserUpdateRequest;
import com.ugts.user.dto.response.AddressResponse;
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

    /**
     * Retrieves all users.
     *
     * @return         ApiResponse containing a list of UserResponse objects
     */
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    /**
     * Retrieves the user information for the specified user ID.
     *
     * @param  userId  the ID of the user to retrieve information for
     * @return         an ApiResponse containing the user information, or an error message if the user is not found
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .message("Success")
                .result(userService.getUserById(userId))
                .build();
    }

    /**
     * Get the profile information of the user.
     *
     * @return          ApiResponse containing the user profile information
     */
    @GetMapping("/profile")
    public ApiResponse<UserResponse> getProfile() {
        return ApiResponse.<UserResponse>builder()
                .message("Success")
                .result(userService.getProfile())
                .build();
    }

    /**
     * Updates user information for a specific user.
     *
     * @param  userId   the ID of the user to update
     * @param  request  the request containing the updated user information
     * @return          the response containing the updated user information
     */
    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUserInfo(
            @PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("Update Success")
                .result(userService.updateUserInfo(userId, request))
                .build();
    }

    /**
     * Updates the user's avatar based on the provided avatar file.
     *
     * @param  userId  the ID of the user whose avatar is being updated
     * @param  avatar  the avatar file to update
     * @return         the response containing the updated user information
     */
    @PutMapping("/{userId}/avatar")
    public ApiResponse<UserResponse> updateUserAvatar(
            @PathVariable String userId, @RequestPart("avatar") MultipartFile avatar) throws IOException {
        return ApiResponse.<UserResponse>builder()
                .message("Update Avatar Success")
                .result(userService.updateUserAvatar(userId, avatar))
                .build();
    }

    /**
     * Creates a new address for a user.
     *
     * @param  userId     the ID of the user
     * @param  request    the request containing the details of the new address
     * @return            the response containing the details of the newly created address
     */
    @PostMapping("/address")
    public ApiResponse<AddressResponse> createNewAddress(
            @RequestParam String userId, @RequestBody CreateNewAddressRequest request) {
        var result = userService.createNewAddress(userId, request);
        return ApiResponse.<AddressResponse>builder()
                .message("Create New Address Success")
                .result(result)
                .build();
    }

    /**
     * Updates the user address with the specified addressId for the given userId.
     *
     * @param  userId     the user ID
     * @param  addressId  the ID of the address to update
     * @param  request    the updated address request
     * @return            the response containing the updated address details
     */
    @PutMapping("/address")
    public ApiResponse<AddressResponse> updateAddress(
            @RequestParam String userId, @RequestParam Long addressId, @RequestBody UpdateAddressRequest request) {
        var result = userService.updateUserAddress(userId, addressId, request);
        return ApiResponse.<AddressResponse>builder()
                .message("Update Address Success")
                .result(result)
                .build();
    }

    /**
     * Sets the default address for a user.
     *
     * @param  userId    the user ID
     * @param  addressId the address ID to set as default
     * @return           the address response indicating the success of setting the default address
     */
    @PutMapping("/address/default")
    public ApiResponse<AddressResponse> setDefaultAddress(@RequestParam String userId, @RequestParam Long addressId) {
        var result = userService.setDefaultAddress(userId, addressId);
        return ApiResponse.<AddressResponse>builder()
                .message("Set Default Address Success")
                .result(result)
                .build();
    }
}
