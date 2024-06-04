package com.ugts.post.controller;

import java.io.IOException;

import com.ugts.dto.ApiResponse;
import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @RequestPart CreatePostRequest request, @RequestPart("productImage") MultipartFile[] productImages)
            throws IOException {
        var result = postService.createPost(request, productImages);
        return ApiResponse.<PostResponse>builder()
                .message("Create Success")
                .result(result)
                .build();
    }
}
