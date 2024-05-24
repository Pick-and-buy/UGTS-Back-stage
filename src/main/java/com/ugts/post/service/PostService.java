package com.ugts.post.service;

import java.io.IOException;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostResponse createPost(CreatePostRequest request, MultipartFile productImage) throws IOException;
}
