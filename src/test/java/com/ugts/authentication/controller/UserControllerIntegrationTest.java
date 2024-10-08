package com.ugts.authentication.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.user.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    private RegisterRequest request;
    private UserResponse response;

    @BeforeEach
    public void initData() {
        LocalDateTime localDateTime = LocalDateTime.of(2002, 10, 1, 0, 0);

        Date dob = Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());

        request = RegisterRequest.builder()
                .username("test03")
                .firstName("quang")
                .lastName("tran")
                .email("test03@gmail.com")
                .password("Quang09122002@")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .id("42e2-bae5-9ea7c0f1c4d455")
                .username("test03")
                .firstName("quang")
                .lastName("tran")
                .email("test03@gmail.com")
                .dob(dob)
                .build();
    }

    @Test
    void register_validRequest_success() throws Exception {
        // GIVEN (valid request)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk()) // status of API response
                .andExpect(jsonPath("code").value(1000)) // code of API response
                .andExpect(jsonPath("result.username").value("test03")) // Result of API response
                .andExpect(jsonPath("result.firstName").value("quang"))
                .andExpect(jsonPath("result.lastName").value("tran"))
                .andExpect(jsonPath("result.email").value("test03@gmail.com"))
                .andExpect(jsonPath("result.dob").value("2002-10-01"));
    }
}
