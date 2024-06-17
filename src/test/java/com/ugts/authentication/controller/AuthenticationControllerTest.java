package com.ugts.authentication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.authentication.service.AuthenticationService;
import com.ugts.user.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // mock to authentication service
    private AuthenticationService authenticationService;

    private RegisterRequest request;
    private UserResponse response;

    @BeforeEach
    public void initData() {
        Date dob = new Date(2000, 1, 1);

        request = RegisterRequest.builder()
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .password("Quang09122002@")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .id("42e2-bae5-9ea7c0f1c4d4")
                .username("test02")
                .firstName("quang")
                .lastName("tran")
                .email("test02@gmail.com")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN (valid request)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        when(authenticationService.register(any())) // instead of call to createUser() in UserService
                .thenReturn(response); // using mock to mock userService.createUser() for using in test

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk()) // status of API response
                .andExpect(jsonPath("code").value(1000)) // code of API response
                .andExpect(jsonPath("result.id").value("42e2-bae5-9ea7c0f1c4d4")) // Result of API response
                .andExpect(jsonPath("result.username").value("test02")) // Result of API response
                .andExpect(jsonPath("result.firstName").value("quang"))
                .andExpect(jsonPath("result.lastName").value("tran"))
                .andExpect(jsonPath("result.email").value("test02@gmail.com"))
                .andExpect(jsonPath("result.dob").value("2000-01-01"));
    }

    @Test
    void createUser_usernameInValidRequest_success() throws Exception {
        // GIVEN (invalid request)
        request.setUsername("tes");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // when(authenticationService.register(any())).thenReturn(response);
        // Because method return response of
        // @Size of Validation library (exception)
        // So don't need to mock to the Service

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest()) // status of API response
                .andExpect(jsonPath("code").value(1003)) // code of API response
                .andExpect(jsonPath("message")
                        .value("Username must be at least 4 characters!")); // message of API response
    }
}
