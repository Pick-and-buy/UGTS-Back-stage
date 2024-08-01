package com.ugts.user.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.entity.NotificationType;
import com.ugts.notification.service.NotificationServiceImpl;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.dto.request.CreateNewAddressRequest;
import com.ugts.user.dto.request.LikeRequestDto;
import com.ugts.user.dto.request.UpdateAddressRequest;
import com.ugts.user.dto.request.UserUpdateRequest;
import com.ugts.user.dto.response.AddressResponse;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.Address;
import com.ugts.user.entity.User;
import com.ugts.user.mapper.AddressMapper;
import com.ugts.user.mapper.UserMapper;
import com.ugts.user.repository.AddressRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PostRepository postRepository;

    PostMapper postMapper;

    private final NotificationServiceImpl notificationService;

    GoogleCloudStorageService googleCloudStorageService;

    AddressMapper addressMapper;

    AddressRepository addressRepository;

    /**
     * Retrieves all users from the repository and maps them to UserResponse objects.
     *
     * @return          a list of UserResponse objects representing all users
     */
    @PreAuthorize("hasRole('ADMIN')") // verify that the user is ADMIN before getAllUsers() is called
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponse)
                .toList();
    }

    /**
     * Retrieves a user by userId and maps it to a UserResponse object.
     *
     * @param  userId   the unique identifier of the user to retrieve
     * @return          the UserResponse object corresponding to the user found
     */
    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String userId) {
        return userMapper.userToUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    /**
     * Retrieves the user profile of the currently authenticated user.
     *
     * @return          The user profile as a UserResponse object.
     * @throws AppException if the user does not exist.
     */
    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserResponse getProfile() {
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.userToUserResponse(user);
    }

    /**
     * Likes a post for a user.
     *
     * @param  likeRequestDto   the DTO containing the user ID and post ID
     * @throws AppException    if the user or post does not exist or if the post is already liked
     */
    @PreAuthorize("hasAnyRole('USER')")
    public void likePost(LikeRequestDto likeRequestDto) {
        User likeUser = userRepository
                .findById(likeRequestDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository
                .findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (likeUser.getLikedPosts().contains(post)) {
            throw new AppException(ErrorCode.POST_ALREADY_LIKED);
        }
        likeUser.getLikedPosts().add(post);

        // Notify to user
        User userToNotify = post.getUser();
        if (!userToNotify.getId().equals(likeUser.getId())) {
            notificationService.createNotificationStorage(NotificationEntity.builder()
                    .delivered(false)
                    .message("Lượt yêu thích mới từ " + likeUser.getUsername())
                    .notificationType(NotificationType.LIKE)
                    .userFromId(likeUser.getId())
                    .userToId(userToNotify.getId())
                    .timestamp(new Date())
                    .userFromAvatar(likeUser.getAvatar())
                    .postId(post.getId())
                    .build());
        }
        userRepository.save(likeUser);
    }

    /**
     * Removes a post from the list of liked posts for a user.
     *
     * @param  likeRequestDto  the DTO containing the user ID and post ID
     * @throws AppException    if the user or post does not exist or if the post is already unliked
     */
    @PreAuthorize("hasAnyRole('USER')")
    @Override
    public void unlikePost(LikeRequestDto likeRequestDto) {
        User user = userRepository
                .findById(likeRequestDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository
                .findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (!user.getLikedPosts().contains(post)) {
            throw new AppException(ErrorCode.POST_ALREADY_UNLIKED);
        }
        user.getLikedPosts().remove(post);
        userRepository.save(user);
    }

    /**
     * Retrieves the liked posts for a user identified by the provided userId.
     *
     * @param  userId    the ID of the user for whom to retrieve liked posts
     * @return           a list of PostResponse objects representing the liked posts
     */
    @PreAuthorize("hasAnyRole('USER')")
    @Override
    public List<PostResponse> getLikedPosts(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Post> likedPosts = postRepository.findPostsLikedByUser(user.getId());
        return likedPosts.stream().map(postMapper::postToPostResponse).toList();
    }

    /**
     * Updates the user information based on the provided UserUpdateRequest.
     *
     * @param  userId    the ID of the user to be updated
     * @param  request   the UserUpdateRequest containing the new user information
     * @return           a UserResponse object representing the updated user information
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public UserResponse updateUserInfo(String userId, UserUpdateRequest request) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setDob(request.getDob());

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    /**
     * Updates the user's avatar by uploading the provided MultipartFile to Google Cloud Storage and
     * updating the user's avatar URL in the database.
     *
     * @param  userId    the ID of the user whose avatar is being updated
     * @param  file      the MultipartFile containing the avatar image
     * @return           the UserResponse containing the updated user information
     * @throws IOException if there is an error uploading the file to Google Cloud Storage
     */
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Override
    public UserResponse updateUserAvatar(String userId, MultipartFile file) throws IOException {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String avatarUrl = googleCloudStorageService.uploadUserAvatarToGCS(file, userId);
        user.setAvatar(avatarUrl);

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    /**
     * Creates a new address for a user based on the provided parameters.
     *
     * @param  userId                     The ID of the user for whom the address is being created
     * @param  createNewAddressRequest     The request object containing the new address details
     * @return                            The response containing the newly created address information
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AddressResponse createNewAddress(String userId, CreateNewAddressRequest createNewAddressRequest) {
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var address = Address.builder()
                .street(createNewAddressRequest.getStreet())
                .district(createNewAddressRequest.getDistrict())
                .province(createNewAddressRequest.getProvince())
                .country(createNewAddressRequest.getCountry())
                .addressLine(createNewAddressRequest.getAddressLine())
                .user(user)
                .build();

        return addressMapper.toAddress(addressRepository.save(address));
    }

    /**
     * Updates the user's address based on the provided user ID, address ID, and update address request.
     *
     * @param  userId         the ID of the user whose address is to be updated
     * @param  addressId      the ID of the address to be updated
     * @param  updateAddressRequest  the request containing the updated address information
     * @return                the updated AddressResponse after updating the user's address
     * @throws AppException  if the user or address does not exist
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AddressResponse updateUserAddress(String userId, Long addressId, UpdateAddressRequest updateAddressRequest) {
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var address = user.getAddress().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        address.setStreet(updateAddressRequest.getStreet());
        address.setDistrict(updateAddressRequest.getDistrict());
        address.setProvince(updateAddressRequest.getProvince());
        address.setCountry(updateAddressRequest.getCountry());
        address.setAddressLine(updateAddressRequest.getAddressLine());

        user.setAddress(Set.of(address));

        return addressMapper.toAddress(addressRepository.save(address));
    }

    /**
     * Sets the default address for a user based on the provided user ID and address ID.
     *
     * @param  userId     the ID of the user for whom the default address is being set
     * @param  addressId  the ID of the address to be set as the default
     * @return            the updated AddressResponse after setting the default address
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AddressResponse setDefaultAddress(String userId, Long addressId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Find the current default address for the user
        Optional<Address> currentDefaultAddressOpt = addressRepository.findByUserIdAndDefault(user.getId(), true);

        if (currentDefaultAddressOpt.isPresent()) {
            var currentDefaultAddress = currentDefaultAddressOpt.get();

            if (!currentDefaultAddress.getId().equals(addressId)) {
                // Update the previous default address to false
                currentDefaultAddress.setDefault(false);
                addressRepository.save(currentDefaultAddress);
            }
        }

        var address = addressRepository
                .findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        // Update the new default address
        address.setDefault(true);
        addressRepository.save(address);

        return addressMapper.toAddress(address);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteAddress(Long addressId) {
        var address = addressRepository
                .findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));
        if (address != null) {
            addressRepository.deleteById(addressId);
        }
    }
}
