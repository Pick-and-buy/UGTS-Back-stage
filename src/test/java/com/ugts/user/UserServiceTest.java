// package com.ugts.user;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
//
// import java.io.IOException;
// import java.util.*;
//
// import com.ugts.authentication.dto.request.RegisterRequest;
// import com.ugts.cloudService.GoogleCloudStorageService;
// import com.ugts.exception.AppException;
// import com.ugts.exception.ErrorCode;
// import com.ugts.post.dto.response.PostResponse;
// import com.ugts.post.entity.Post;
// import com.ugts.post.mapper.PostMapper;
// import com.ugts.post.repository.PostRepository;
// import com.ugts.user.controller.UserController;
// import com.ugts.user.dto.request.CreateNewAddressRequest;
// import com.ugts.user.dto.request.LikeRequestDto;
// import com.ugts.user.dto.request.UpdateAddressRequest;
// import com.ugts.user.dto.request.UserUpdateRequest;
// import com.ugts.user.dto.response.AddressResponse;
// import com.ugts.user.dto.response.UserResponse;
// import com.ugts.user.entity.Address;
// import com.ugts.user.entity.User;
// import com.ugts.user.mapper.AddressMapper;
// import com.ugts.user.mapper.UserMapper;
// import com.ugts.user.repository.AddressRepository;
// import com.ugts.user.repository.UserRepository;
// import com.ugts.user.service.UserService;
// import com.ugts.user.service.impl.UserServiceImpl;
// import jakarta.validation.ConstraintViolationException;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.web.multipart.MultipartFile;
//
// @SpringBootTest
// @TestPropertySource("/test.properties")
// public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    private RegisterRequest request;
//
//    private UserResponse response;
//
//    private User user;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    UserMapper userMapper;
//
//    @Mock
//    PostRepository postRepository;
//
//    @Mock
//    PostMapper postMapper;
//
//    @Mock
//    GoogleCloudStorageService googleCloudStorageService;
//
//    @Mock
//    AddressMapper addressMapper;
//
//    @Mock
//    AddressRepository addressRepository;
//
//    /**
//     * Test method to retrieve all users when the user is an admin.
//     */
////    @Test
////    @WithMockUser(roles = "ADMIN")
////    public void retrieves_all_users_when_admin() {
////        // Given
////        UserServiceImpl userService = new UserServiceImpl(
////                userRepository,
////                userMapper,
////                postRepository,
////                postMapper,
////                googleCloudStorageService,
////                addressMapper,
////                addressRepository);
////        List<User> users = List.of(new User(), new User());
////        when(userRepository.findAll()).thenReturn(users);
////        when(userMapper.userToUserResponse(any(User.class))).thenReturn(new UserResponse());
////
////        // When
////        List<UserResponse> result = userService.getAllUsers();
////
////        // Then
////        assertEquals(users.size(), result.size());
////    }
//
//    /**
//     * Test method to handle empty user repository.
//     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void handles_empty_user_repository() {
//        // Given
//        UserServiceImpl userService = new UserServiceImpl(
//                userRepository,
//                userMapper,
//                postRepository,
//                postMapper,
//                googleCloudStorageService,
//                addressMapper,
//                addressRepository);
//        when(userRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // When
//        List<UserResponse> result = userService.getAllUsers();
//
//        // Then
//        assertTrue(result.isEmpty());
//    }
//
//    /**
//     * Test method for handling user mapper exception.
//     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void handles_user_mapper_exception() {
//        // Given
//        UserServiceImpl userService = new UserServiceImpl(
//                userRepository,
//                userMapper,
//                postRepository,
//                postMapper,
//                googleCloudStorageService,
//                addressMapper,
//                addressRepository);
//        List<User> users = List.of(new User());
//        when(userRepository.findAll()).thenReturn(users);
//        when(userMapper.userToUserResponse(any(User.class))).thenThrow(new RuntimeException("Mapping error"));
//
//        // When / Then
//        assertThrows(RuntimeException.class, userService::getAllUsers);
//    }
//
//    /**
//     * Test method for retrieving a user by a valid userId.
//     *
//     * This test method creates a mock UserRepository and UserMapper objects,
//     * and initializes a UserServiceImpl object with these mock objects.
//     * It then sets up the mock UserRepository to return a valid User object
//     * when given a specific userId. The User object has a username of "testUser".
//     * The test method also sets up the mock UserMapper to return a UserResponse
//     * object when given a User object. The UserResponse object has a username
//     * of "testUser".
//     *
//     * The test method then calls the getUserById method of the UserServiceImpl
//     * object with the specified userId. It asserts that the returned UserResponse
//     * object has the same username as the expectedResponse object.
//     *
//     * @return void
//     */
//    @Test
//    public void test_retrieves_user_by_valid_userId() {
//        // Given
//        UserRepository userRepository = mock(UserRepository.class);
//        UserMapper userMapper = mock(UserMapper.class);
//        UserServiceImpl userService = new UserServiceImpl(userRepository, userMapper, null, null, null, null, null);
//        String userId = "validUserId";
//        User user = new User();
//        user.setUsername("testUser");
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        UserResponse expectedResponse = new UserResponse();
//        expectedResponse.setUsername("testUser");
//        when(userMapper.userToUserResponse(user)).thenReturn(expectedResponse);
//
//        // When
//        UserResponse actualResponse = userService.getUserById(userId);
//
//        // Then
//        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
//    }
//
//    /**
//     * Test case to verify the behavior when the specified user ID does not exist in the repository.
//     *
//     * @throws AppException if the user ID is not found in the repository
//     */
//    @Test
//    public void test_userId_does_not_exist_in_repository() {
//        // Given
//        UserRepository userRepository = mock(UserRepository.class);
//        UserMapper userMapper = mock(UserMapper.class);
//        UserServiceImpl userService = new UserServiceImpl(userRepository, userMapper, null, null, null, null, null);
//        String userId = "nonExistentUserId";
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.getUserById(userId);
//        });
//        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
//    }
//
//    /**
//     * Test case to verify the success of unlike a post for a user.
//     *
//     * This test case sets up a mocked UserRepository and PostRepository, as well as a UserServiceImpl instance.
//     * It creates a User and a Post, adds the Post to the User's likedPosts list, and creates a LikeRequestDto
//     * with the user and post IDs.
//     *
//     * The test then mocks the findById method of the UserRepository and PostRepository to return the User and Post
//     * respectively when called with the specified IDs.
//     *
//     * The test then calls the unlikePost method of the UserServiceImpl instance with the LikeRequestDto, which
//     * removes the Post from the User's likedPosts list and saves the User.
//     *
//     * Finally, the test asserts that the Post is no longer in the User's likedPosts list and verifies that the
//     * save method of the UserRepository was called with the User.
//     *
//     * @throws AppException if the user or post does not exist or if the post is already unliked
//     */
//    @Test
//    public void test_unlike_post_success() {
//        // Given
//        UserRepository userRepository = mock(UserRepository.class);
//        PostRepository postRepository = mock(PostRepository.class);
//        UserServiceImpl userService = new UserServiceImpl(userRepository, null, postRepository, null, null, null,
// null);
//
//        User user = new User();
//        user.setId("user1");
//        Post post = new Post();
//        post.setId("post1");
//        user.getLikedPosts().add(post);
//
//        LikeRequestDto likeRequestDto = new LikeRequestDto("user1", "post1");
//
//        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
//        when(postRepository.findById("post1")).thenReturn(Optional.of(post));
//
//        // When
//        userService.unlikePost(likeRequestDto);
//
//        // Then
//        assertFalse(user.getLikedPosts().contains(post));
//        verify(userRepository).save(user);
//    }
//
//    /**
//     * Test case to verify the behavior when the specified user does not exist in the repository.
//     *
//     * @return void
//     */
//    @Test
//    public void test_unlike_post_user_not_existed() {
//        // Given
//        UserRepository userRepository = mock(UserRepository.class);
//        PostRepository postRepository = mock(PostRepository.class);
//        UserServiceImpl userService = new UserServiceImpl(userRepository, null, postRepository, null, null, null,
// null);
//
//        LikeRequestDto likeRequestDto = new LikeRequestDto("user1", "post1");
//
//        when(userRepository.findById("user1")).thenReturn(Optional.empty());
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.unlikePost(likeRequestDto);
//        });
//
//        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
//    }
//
//    /**
//     * Test case to verify the behavior when the specified post does not exist in the repository.
//     *
//     * @return void
//     */
//    @Test
//    public void test_unlike_post_post_not_existed() {
//        // Given
//        UserRepository userRepository = mock(UserRepository.class);
//        PostRepository postRepository = mock(PostRepository.class);
//        UserServiceImpl userService = new UserServiceImpl(userRepository, null, postRepository, null, null, null,
// null);
//
//        User user = new User();
//        user.setId("user1");
//
//        LikeRequestDto likeRequestDto = new LikeRequestDto("user1", "post1");
//
//        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
//        when(postRepository.findById("post1")).thenReturn(Optional.empty());
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.unlikePost(likeRequestDto);
//        });
//
//        assertEquals(ErrorCode.POST_NOT_EXISTED, exception.getErrorCode());
//    }
//
//    /**
//     * Test method to retrieve liked posts for a valid user ID.
//     * It sets up mock repositories, mocks the behavior for finding liked posts by user,
//     * and asserts that the correct number of liked posts is returned.
//     */
//    @Test
//    public void retrieves_liked_posts_for_valid_user_id() {
//        // Given
//        String userId = "validUserId";
//        User user = new User();
//        user.setId(userId);
//        Post post1 = new Post();
//        post1.setId("post1");
//        Post post2 = new Post();
//        post2.setId("post2");
//        List<Post> likedPosts = List.of(post1, post2);
//
//        UserRepository userRepository = mock(UserRepository.class);
//        PostRepository postRepository = mock(PostRepository.class);
//        PostMapper postMapper = mock(PostMapper.class);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(postRepository.findPostsLikedByUser(userId)).thenReturn(likedPosts);
//        when(postMapper.postToPostResponse(post1)).thenReturn(new PostResponse());
//        when(postMapper.postToPostResponse(post2)).thenReturn(new PostResponse());
//
//        UserServiceImpl userService =
//                new UserServiceImpl(userRepository, null, postRepository, postMapper, null, null, null);
//
//        // When
//        List<PostResponse> result = userService.getLikedPosts(userId);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
//
//    /**
//     * Test case to verify the behavior when the specified user ID does not exist in the repository.
//     *
//     * @throws AppException if the user ID is not found in the repository
//     */
//    @Test
//    public void throws_app_exception_if_user_id_does_not_exist() {
//        // Given
//        String userId = "nonExistentUserId";
//
//        UserRepository userRepository = mock(UserRepository.class);
//        PostRepository postRepository = mock(PostRepository.class);
//        PostMapper postMapper = mock(PostMapper.class);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        UserServiceImpl userService =
//                new UserServiceImpl(userRepository, null, postRepository, postMapper, null, null, null);
//
//        // When & Then
//        assertThrows(AppException.class, () -> userService.getLikedPosts(userId));
//    }
//
//    /**
//     * Test to verify the function handles null or empty user ID input gracefully.
//     *
//     * @return void
//     */
//    @Test
//    public void handles_null_or_empty_user_id_input_gracefully() {
//        // Given
//        String userId = "";
//
//        UserRepository userRepository = mock(UserRepository.class);
//        PostRepository postRepository = mock(PostRepository.class);
//        PostMapper postMapper = mock(PostMapper.class);
//
//        UserServiceImpl userService =
//                new UserServiceImpl(userRepository, null, postRepository, postMapper, null, null, null);
//
//        // When & Then
//        assertThrows(AppException.class, () -> userService.getLikedPosts(userId));
//    }
//
//    /**
//     * Test to verify the successful update of user information.
//     *
//     * @return None
//     */
//    @Test
//    @WithMockUser(
//            username = "test02",
//            roles = {"USER"})
//    public void test_successful_update_user_info() {
//        // Given
//        String userId = "bf1ca931-8685-4ea7-b554-85926ec6e9e5";
//        UserUpdateRequest request = UserUpdateRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .username("johndoe")
//                .email("john.doe@example.com")
//                .dob(new Date())
//                .build();
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(userMapper.userToUserResponse(any(User.class))).thenReturn(new UserResponse());
//
//        // When
//        UserResponse response = userService.updateUserInfo(userId, request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("John", user.getFirstName());
//        assertEquals("Doe", user.getLastName());
//        assertEquals("johndoe", user.getUsername());
//        assertEquals("john.doe@example.com", user.getEmail());
//    }
//
//    /**
//     * Test case to verify the behavior when the specified user ID does not exist in the repository.
//     *
//     * This test case sets up a mocked UserRepository and UserServiceImpl instance. It creates a UserUpdateRequest
//     * object with a non-existent user ID and a set of test user information. It sets up the mock UserRepository to
//     * return an empty Optional when given the specified user ID.
//     *
//     * The test case then calls the updateUserInfo method of the UserServiceImpl object with the specified user ID
//     * and the UserUpdateRequest object. It asserts that an AppException is thrown with the error code
//     * USER_NOT_EXISTED.
//     *
//     * @throws AppException if the user ID is not found in the repository
//     */
//    @Test
//    public void test_update_user_info_user_not_exist() {
//        // Given
//        String userId = "bf1ca931-8685-4ea7-b554-85926ec";
//        UserUpdateRequest request = UserUpdateRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .username("johndoe")
//                .email("john.doe@example.com")
//                .dob(new Date())
//                .build();
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.updateUserInfo(userId, request);
//        });
//        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
//    }
//
//    /**
//     * Test case for updating user information with null values.
//     *
//     * This test verifies that the user information is not updated when the update request contains null values.
//     * The test sets up a mock user repository and mapper to simulate the behavior of the actual implementation.
//     * It then calls the `updateUserInfo` method of the `UserService` with a user ID and a user update request
// containing null values.
//     * The test asserts that the response is not null and that the user's first name, last name, username, email, and
// date of birth are all null.
//     *
//     * @throws Exception if an error occurs during the test execution
//     */
//    @Test
//    public void test_update_user_info_with_null_values() {
//        // Given
//        String userId = "validUserId";
//        UserUpdateRequest request = UserUpdateRequest.builder()
//                .firstName(null)
//                .lastName(null)
//                .username(null)
//                .email(null)
//                .dob(null)
//                .build();
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        when(userMapper.userToUserResponse(any(User.class))).thenReturn(new UserResponse());
//
//        // When
//        UserResponse response = userService.updateUserInfo(userId, request);
//
//        // Then
//        assertNotNull(response);
//        assertNull(user.getFirstName());
//        assertNull(user.getLastName());
//        assertNull(user.getUsername());
//        assertNull(user.getEmail());
//        assertNull(user.getDob());
//    }
//
//    @Test
//    public void test_successful_avatar_update() throws IOException {
//        // Given
//        String userId = "validUserId";
//        MultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[] {1, 2, 3});
//        User user = new User();
//        user.setId(userId);
//        user.setUsername("testUser");
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(googleCloudStorageService.uploadUserAvatarToGCS(file,
// userId)).thenReturn("http://example.com/avatar.png");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        // When
//        UserResponse response = userService.updateUserAvatar(userId, file);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("http://example.com/avatar.png", response.getAvatar());
//    }
//
//    @Test
//    public void test_user_not_exist_exception() {
//        // Given
//        String userId = "nonExistentUserId";
//        MultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[] {1, 2, 3});
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(AppException.class, () -> {
//            userService.updateUserAvatar(userId, file);
//        });
//    }
//
//    @Test
//    public void test_io_exception_on_upload() throws IOException {
//        // Given
//        String userId = "validUserId";
//        MultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[] {1, 2, 3});
//        User user = new User();
//        user.setId(userId);
//        user.setUsername("testUser");
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(googleCloudStorageService.uploadUserAvatarToGCS(file, userId)).thenThrow(new IOException("Upload
// failed"));
//
//        // When & Then
//        assertThrows(IOException.class, () -> {
//            userService.updateUserAvatar(userId, file);
//        });
//    }
//
//    /**
//     * Test case for the success scenario of creating a new address.
//     *
//     * @return void
//     */
//    @Test
//    @WithMockUser(
//            username = "test02",
//            roles = {"USER"})
//    public void test_create_new_address_success() {
//        // Given
//        String userId = "user123";
//        CreateNewAddressRequest request = new CreateNewAddressRequest();
//        request.setStreet("123 Main St");
//        request.setDistrict("Central");
//        request.setProvince("Province");
//        request.setCountry("Country");
//        request.setAddressLine("Address Line");
//
//        User user = new User();
//        user.setPhoneNumber("1234567890");
//
//        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
//        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(addressMapper.toAddress(any(Address.class))).thenReturn(new AddressResponse());
//
//        // When
//        AddressResponse response = userService.createNewAddress(userId, request);
//
//        // Then
//        assertNotNull(response);
//        verify(userRepository, times(1)).findByPhoneNumber(anyString());
//        verify(addressRepository, times(1)).save(any(Address.class));
//    }
//
//    @Test
//    public void test_create_new_address_user_not_exist() {
//        // Given
//        String userId = "user123";
//        CreateNewAddressRequest request = new CreateNewAddressRequest();
//        request.setStreet("123 Main St");
//        request.setDistrict("Central");
//        request.setProvince("Province");
//        request.setCountry("Country");
//        request.setAddressLine("Address Line");
//
//        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.createNewAddress(userId, request);
//        });
//
//        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
//        verify(userRepository, times(1)).findByPhoneNumber(anyString());
//    }
//
//    @Test
//    public void test_create_new_address_invalid_details() {
//        // Given
//        String userId = "user123";
//        CreateNewAddressRequest request = new CreateNewAddressRequest();
//        request.setStreet(null); // Invalid street
//
//        User user = new User();
//        user.setPhoneNumber("1234567890");
//
//        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
//
//        // When & Then
//        assertThrows(ConstraintViolationException.class, () -> {
//            userService.createNewAddress(userId, request);
//        });
//
//        verify(userRepository, times(1)).findByPhoneNumber(anyString());
//    }
//
//    @Test
//    public void test_successful_address_update() {
//        // Given
//        String userId = "bf1ca931-8685-4ea7-b554-85926ec6e9e5";
//        Long addressId = 1L;
//        UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest(
//                "New Street", "New District", "New Province", "New Country", "New Address Line");
//        UserServiceImpl userService = new UserServiceImpl(
//                userRepository,
//                userMapper,
//                postRepository,
//                postMapper,
//                googleCloudStorageService,
//                addressMapper,
//                addressRepository);
//
//        // Mocking the necessary data
//        User user = new User();
//        user.setPhoneNumber("0563016466");
//        Address address = new Address();
//        address.setId(addressId);
//        user.setAddress(Set.of(address));
//
//        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
//        when(addressRepository.save(any(Address.class))).thenReturn(address);
//
//        // When
//        AddressResponse response = userService.updateUserAddress(userId, addressId, updateAddressRequest);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("New Street", response.getStreet());
//        assertEquals("New District", response.getDistrict());
//        assertEquals("New Province", response.getProvince());
//        assertEquals("New Country", response.getCountry());
//        assertEquals("New Address Line", response.getAddressLine());
//    }
//
//    /**
//     * Test case for handling a non-existent user ID scenario.
//     */
//    @Test
//    public void test_non_existent_user_id() {
//        // Given
//        String userId = "nonExistentUserId";
//        Long addressId = 1L;
//        UpdateAddressRequest request =
//                new UpdateAddressRequest("123 Street", "District", "Province", "Country", "Address Line");
//        UserService userService = mock(UserService.class);
//        when(userService.updateUserAddress(userId, addressId, request))
//                .thenThrow(new RuntimeException("User not found"));
//        UserController userController = new UserController(userService);
//
//        // When
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            userController.updateAddress(userId, addressId, request);
//        });
//
//        // Then
//        assertEquals("User not found", exception.getMessage());
//    }
//
//    /**
//     * Test case for handling a non-existent address ID scenario.
//     */
//    @Test
//    public void test_non_existent_address_id() {
//        // Given
//        String userId = "validUserId";
//        Long addressId = 999L;
//        UpdateAddressRequest request =
//                new UpdateAddressRequest("123 Street", "District", "Province", "Country", "Address Line");
//        UserService userService = mock(UserService.class);
//        when(userService.updateUserAddress(userId, addressId, request))
//                .thenThrow(new RuntimeException("Address not found"));
//        UserController userController = new UserController(userService);
//
//        // When
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            userController.updateAddress(userId, addressId, request);
//        });
//
//        // Then
//        assertEquals("Address not found", exception.getMessage());
//    }
//
//    /**
//     * Test case for setting the default address successfully.
//     */
//    @Test
//    public void test_set_default_address_success() {
//        // Given
//        String userId = "user123";
//        Long addressId = 1L;
//        User user = new User();
//        user.setId(userId);
//        Address address = new Address();
//        address.setId(addressId);
//        address.setUser(user);
//        address.setDefault(false);
//
//        UserRepository userRepository = mock(UserRepository.class);
//        AddressRepository addressRepository = mock(AddressRepository.class);
//        AddressMapper addressMapper = mock(AddressMapper.class);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(addressRepository.findByUserIdAndDefault(userId, true)).thenReturn(Optional.empty());
//        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
//        when(addressMapper.toAddress(any(Address.class))).thenReturn(new AddressResponse());
//
//        UserServiceImpl userService =
//                new UserServiceImpl(userRepository, null, null, null, null, addressMapper, addressRepository);
//
//        // When
//        AddressResponse response = userService.setDefaultAddress(userId, addressId);
//
//        // Then
//        assertNotNull(response);
//        assertTrue(address.isDefault());
//    }
//
//    /**
//     * Test case for setting the default address when the user does not exist.
//     *
//     * This test method creates a mock UserRepository and AddressMapper objects,
//     * and initializes a UserServiceImpl object with these mock objects.
//     * It then sets up the mock UserRepository to return an empty Optional when
//     * given a specific userId.
//     *
//     * The test method then calls the setDefaultAddress method of the UserServiceImpl
//     * object with the specified userId and addressId. It asserts that an AppException
//     * is thrown with the error code ErrorCode.USER_NOT_EXISTED.
//     *
//     * @return void
//     */
//    @Test
//    public void test_set_default_address_user_not_exist() {
//        // Given
//        String userId = "user123";
//        Long addressId = 1L;
//
//        UserRepository userRepository = mock(UserRepository.class);
//        AddressRepository addressRepository = mock(AddressRepository.class);
//        AddressMapper addressMapper = mock(AddressMapper.class);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        UserServiceImpl userService =
//                new UserServiceImpl(userRepository, null, null, null, null, addressMapper, addressRepository);
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.setDefaultAddress(userId, addressId);
//        });
//
//        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
//    }
//
//    /**
//     * Test case for setting the default address when the address does not exist.
//     */
//    @Test
//    public void test_set_default_address_address_not_exist() {
//        // Given
//        String userId = "user123";
//        Long addressId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        UserRepository userRepository = mock(UserRepository.class);
//        AddressRepository addressRepository = mock(AddressRepository.class);
//        AddressMapper addressMapper = mock(AddressMapper.class);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(addressRepository.findByUserIdAndDefault(userId, true)).thenReturn(Optional.empty());
//        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());
//
//        UserServiceImpl userService =
//                new UserServiceImpl(userRepository, null, null, null, null, addressMapper, addressRepository);
//
//        // When & Then
//        AppException exception = assertThrows(AppException.class, () -> {
//            userService.setDefaultAddress(userId, addressId);
//        });
//
//        assertEquals(ErrorCode.ADDRESS_NOT_EXISTED, exception.getErrorCode());
//    }
// }
