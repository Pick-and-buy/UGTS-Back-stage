package com.ugts.post.service;

import java.io.IOException;
import java.util.List;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostResponse createPost(CreatePostRequest postRequest, MultipartFile[] files) throws IOException;

    List<PostResponse> getAllPosts();

    PostResponse updatePost(String id, UpdatePostRequest postRequest);

    PostResponse getPostById(String postId);
}
