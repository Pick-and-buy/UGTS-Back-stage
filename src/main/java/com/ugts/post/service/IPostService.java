package com.ugts.post.service;

import java.io.IOException;
import java.util.List;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.request.UpdatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IPostService {
    PostResponse createPostLevel1(CreatePostRequest postRequest, MultipartFile[] productImages) throws IOException;

    PostResponse createPostLevel2(
            CreatePostRequest postRequest,
            MultipartFile[] productImages,
            MultipartFile productVideo,
            MultipartFile originalReceiptProof)
            throws IOException;

    List<PostResponse> getAllPosts();

    PostResponse updatePost(
            String id,
            UpdatePostRequest postRequest,
            MultipartFile[] productImages,
            MultipartFile productVideo,
            MultipartFile originalReceiptProof)
            throws IOException;

    PostResponse getPostById(String postId);

    List<PostResponse> getPostsByBrand(String brandName);

    List<PostResponse> searchPostsByTitle(String title) throws IOException;

    List<PostResponse> searchPostsByStatus(boolean status) throws IOException;

    List<PostResponse> getPostByUserId(String userId);

    void deletePost(String postId);

    List<PostResponse> getPostByBrandLine(String brandLineName);

    List<PostResponse> getPostsByFollowedUser(String followedUserId);

    void boostPost(String postId, int hours);

    void archivePost(String postId);
}
