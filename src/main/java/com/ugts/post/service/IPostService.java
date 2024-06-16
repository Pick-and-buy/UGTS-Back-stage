package com.ugts.post.service;

import java.io.IOException;
import java.util.List;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

public interface IPostService {
    PostResponse createPost(CreatePostRequest postRequest, MultipartFile[] files) throws IOException;

    List<PostResponse> getAllPosts() throws IOException;

    PostResponse updatePost(String id, UpdatePostRequest postRequest) throws IOException;

    PostResponse getPostById(String postId);

    List<PostResponse> getPostsByBrand(String brandName);

    List<PostResponse> searchPostsByTitle(String title) throws IOException;

    List<PostResponse> searchPostsByStatus(boolean status) throws IOException;
}
