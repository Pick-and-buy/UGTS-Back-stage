package com.ugts.post.mapper;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post createPost(CreatePostRequest request);

    PostResponse postToPostResponse(Post post);
}
