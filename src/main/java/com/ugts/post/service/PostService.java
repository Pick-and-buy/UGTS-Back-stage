package com.ugts.post.service;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;

public interface PostService {
    PostResponse createPost(CreatePostRequest request);
}
