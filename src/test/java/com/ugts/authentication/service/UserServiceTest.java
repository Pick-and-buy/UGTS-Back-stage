package com.ugts.authentication.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.exception.AppException;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/test.properties")
class UserServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean // mock to user repository
    private UserRepository userRepository;

    private RegisterRequest request;
    private UserResponse response;
    private User user;

    @BeforeEach
    public void initData() {
        LocalDate dob = LocalDate.of(2000, 1, 1);

        request = RegisterRequest.builder()
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .password("Quang09122002@")
                .dob(dob)
                .phoneNumber("0563016466")
                .build();

        response = UserResponse.builder()
                .id("42e2-bae5-9ea7c0f1c4d4")
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .dob(dob)
                .phoneNumber("0563016466")
                .build();

        user = User.builder()
                .id("42e2-bae5-9ea7c0f1c4d4")
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .dob(dob)
                .phoneNumber("0563016466")
                .build();
    }

    @Test
    void register_validRequest_success() {
        // GIVEN
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = authenticationService.register(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("42e2-bae5-9ea7c0f1c4d4");
        Assertions.assertThat(response.getPhoneNumber()).isEqualTo("0563016466");
    }

    @Test
    void register_invalidRequest_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> authenticationService.register(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    //    @Test
    //    @WithMockUser(
    //            username = "test02",
    //            roles = {"USER"})
    //    void getProfile_validRequest_success() {
    //        // GIVEN
    //        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
    //
    //        // WHEN
    //        var response = userService.getProfile();
    //
    //        // THEN
    //        Assertions.assertThat(response.getUsername()).isEqualTo("test02");
    //        Assertions.assertThat(response.getId()).isEqualTo("42e2-bae5-9ea7c0f1c4d4");
    //    }

    //    @Test
    //    @WithMockUser(
    //            username = "test02",
    //            roles = {"USER"})
    //    void getProfile_invalidRequest_fail() {
    //        // GIVEN
    //        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.ofNullable(null));
    //
    //        // WHEN
    //        var exception = assertThrows(AppException.class, () -> userService.getProfile());
    //
    //        // THEN
    //        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    //    }
}
