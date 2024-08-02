package com.ugts.post.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugts.dto.ApiResponse;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.IPostRedisService;
import com.ugts.post.service.IPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    IPostRedisService postRedisService;

    PostRepository postRepository;

    PostMapper postMapper;

    @PostMapping(value = "/level-1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPostLevel1(
            @RequestPart("request") String requestJson, @RequestPart("productImages") MultipartFile[] productImages)
            throws IOException {

        // Chuyển đổi JSON string thành đối tượng CreatePostRequest
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        var result = postService.createPostLevel1(request, productImages);
        return ApiResponse.<PostResponse>builder()
                .message("Create Success")
                .result(result)
                .build();
    }

    @PostMapping(value = "/level-2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPostLevel2(
            @RequestPart("request") String requestJson,
            @RequestPart("productImages") MultipartFile[] productImages,
            @RequestPart(value = "productVideo", required = false) MultipartFile productVideo,
            @RequestPart(value = "originalReceiptProof", required = false) MultipartFile originalReceiptProof)
            throws IOException {

        // Chuyển đổi JSON string thành đối tượng CreatePostRequest
        CreatePostRequest request = objectMapper.readValue(requestJson, CreatePostRequest.class);

        var result = postService.createPostLevel2(request, productImages, productVideo, originalReceiptProof);
        return ApiResponse.<PostResponse>builder()
                .message("Create Success")
                .result(result)
                .build();
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
            throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<PostResponse> posts = postRedisService.getAllPosts(pageRequest);

        Map<String, Object> response = new HashMap<>();
        if (posts == null || posts.isEmpty()) {
            Page<Post> postPage = postRepository.findAll(pageRequest);
            posts = postPage.getContent().stream()
                    .map(postMapper::postToPostResponse)
                    .toList();
            postRedisService.saveAllPosts(posts, pageRequest);

            response.put("posts", posts);
            response.put("page", postPage.getNumber());
            response.put("size", postPage.getSize());
            response.put("totalElements", postPage.getTotalElements());
            response.put("totalPages", postPage.getTotalPages());
        } else {
            long totalElements = postRepository.count(); // Or use a method to get the total elements cached
            int totalPages = (int) Math.ceil((double) totalElements / size);

            response.put("posts", posts);
            response.put("page", page);
            response.put("size", size);
            response.put("totalElements", totalElements);
            response.put("totalPages", totalPages);
        }

        return ApiResponse.<Map<String, Object>>builder()
                .message("Get All Post Success")
                .result(response)
                .build();
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> updatePost(
            @PathVariable String postId,
            @RequestPart(value = "request", required = false) String updateRequest,
            @RequestPart(value = "productImages", required = false) MultipartFile[] productImages,
            @RequestPart(value = "productVideo", required = false) MultipartFile productVideo,
            @RequestPart(value = "originalReceiptProof", required = false) MultipartFile originalReceiptProof)
            throws IOException {
        UpdatePostRequest request = objectMapper.readValue(updateRequest, UpdatePostRequest.class);

        var result = postService.updatePost(postId, request, productImages, productVideo, originalReceiptProof);
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
