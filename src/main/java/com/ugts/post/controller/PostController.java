package com.ugts.post.controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugts.dto.ApiResponse;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.service.IPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    IPostService postService;
    ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPost(
            @RequestPart("request") String requestJson, @RequestPart("productImage") MultipartFile[] productImages)
            throws IOException {

        // Chuyển đổi JSON string thành đối tượng CreatePostRequest
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        var result = postService.createPost(request, productImages);
        return ApiResponse.<PostResponse>builder()
                .message("Create Success")
                .result(result)
                .build();
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        var result = postService.getAllPosts();
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get All Post Success")
                .result(result)
                .build();
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable String postId, @RequestBody UpdatePostRequest request)
            throws IOException {
        var result = postService.updatePost(postId, request);
        return ApiResponse.<PostResponse>builder()
                .message("Update Success")
                .result(result)
                .build();
    }

    @GetMapping(value = "/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable String postId) {
        var result = postService.getPostById(postId);
        return ApiResponse.<PostResponse>builder()
                .message("Get Post By Id Success")
                .result(result)
                .build();
    }

    @GetMapping("/brands")
    public ApiResponse<List<PostResponse>> getPostByBrandName(@RequestParam String name) {
        var result = postService.getPostsByBrand(name);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Get Post By Brand Name Success")
                .result(result)
                .build();
    }

    @GetMapping("/user")
    public ApiResponse<List<PostResponse>> getPostsByUserId(@RequestParam String id) {
        var result = postService.getPostByUserId(id);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }

    @GetMapping("/search/{keyword}")
    public ApiResponse<List<PostResponse>> searchPostsByTitle(@PathVariable String keyword) throws IOException {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.searchPostsByTitle(keyword))
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<PostResponse>> searchByStatus(@PathVariable boolean status) throws IOException {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.searchPostsByStatus(status))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deletePost(@RequestParam String postId) {
        postService.deletePost(postId);
        return ApiResponse.<Void>builder().message("Delete success").build();
    }

    @GetMapping("/brandLine")
    public ApiResponse<List<PostResponse>> getPostsByBrandLine(@RequestParam String brandLineName) {
        var result = postService.getPostByBrandLine(brandLineName);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }
    @GetMapping("/followedUser")
    public ApiResponse<List<PostResponse>> getPostByFollowedUser(@RequestParam String followedUserId) {
        var result = postService.getPostsByFollowedUser(followedUserId);
        return ApiResponse.<List<PostResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }
}
