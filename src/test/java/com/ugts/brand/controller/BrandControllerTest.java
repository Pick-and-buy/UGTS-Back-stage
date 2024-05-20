package com.ugts.brand.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.service.BrandService;
import com.ugts.dto.ApiResponse;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class BrandControllerTest {
    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(brandController).build();
    }

    @Test
    void testCreateBrand_success() throws Exception {
        // given
        BrandRequest request = new BrandRequest();
        request.setName("Test Brand");

        BrandResponse response = new BrandResponse();
        response.setId(1L);
        response.setName("Test Brand");

        when(brandService.createBrand(request)).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(brandService).createBrand(request);

        String responseBody = mvcResult.getResponse().getContentAsString();
        ApiResponse<BrandResponse> apiResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
        assertNotNull(apiResponse.getResult());
        assertEquals(response.getId(), apiResponse.getResult().getId());
        assertEquals(response.getName(), apiResponse.getResult().getName());
    }

    @Test
    void testCreateBrand_fail_brandAlreadyExists() throws Exception {
        // given (invalid request)
        BrandRequest request = new BrandRequest();
        request.setName("Test Brand");

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(AppException.class, result.getResolvedException());
                    AppException exception = (AppException) result.getResolvedException();
                    assertEquals(ErrorCode.BRAND_EXISTED, exception.getErrorCode());
                });

        verify(brandService).createBrand(request);
    }

    @Test
    void testGetAllBrands_success() throws Exception {
        // given
        BrandResponse brandResponse1 = new BrandResponse();
        brandResponse1.setId(1L);
        brandResponse1.setName("Brand 1");

        BrandResponse brandResponse2 = new BrandResponse();
        brandResponse2.setId(2L);
        brandResponse2.setName("Brand 2");

        List<BrandResponse> responses = Arrays.asList(brandResponse1, brandResponse2);

        when(brandService.getAllBrands()).thenReturn(responses);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/brands"))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(brandService).getAllBrands();

        String responseBody = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<BrandResponse>> apiResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
        assertNotNull(apiResponse.getResult());
        assertEquals(2, apiResponse.getResult().size());
        assertEquals(brandResponse1.getId(), apiResponse.getResult().get(0).getId());
        assertEquals(brandResponse1.getName(), apiResponse.getResult().get(0).getName());
        assertEquals(brandResponse2.getId(), apiResponse.getResult().get(1).getId());
        assertEquals(brandResponse2.getName(), apiResponse.getResult().get(1).getName());
    }

    @Test
    void testGetBrandByName_success() throws Exception {
        // given
        String name = "Test Brand";

        BrandResponse response = new BrandResponse();
        response.setId(1L);
        response.setName(name);

        when(brandService.getBrandByName(name)).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/brands/{name}", name))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(brandService).getBrandByName(name);

        String responseBody = mvcResult.getResponse().getContentAsString();
        ApiResponse<BrandResponse> apiResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
        assertNotNull(apiResponse.getResult());
        assertEquals(response.getId(), apiResponse.getResult().getId());
        assertEquals(response.getName(), apiResponse.getResult().getName());
    }

    @Test
    void testDeleteBrand_success() throws Exception {
        // given
        String name = "Test Brand";

        // when
        mockMvc.perform(delete("/api/v1/brands/{name}", name))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(brandService).deleteBrand(name);
    }

    @Test
    void testUpdateBrand_success() throws Exception {
        // given
        String name = "Test Brand";
        BrandRequest request = new BrandRequest();
        request.setName("Updated Brand");

        BrandResponse response = new BrandResponse();
        response.setId(1L);
        response.setName(request.getName());

        when(brandService.updateBrand(name, request)).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/brands/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(brandService).updateBrand(name, request);

        String responseBody = mvcResult.getResponse().getContentAsString();
        ApiResponse<BrandResponse> apiResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});
        assertNotNull(apiResponse.getResult());
        assertEquals(response.getId(), apiResponse.getResult().getId());
        assertEquals(response.getName(), apiResponse.getResult().getName());
        assertEquals("Update Success", apiResponse.getMessage());
    }
}
