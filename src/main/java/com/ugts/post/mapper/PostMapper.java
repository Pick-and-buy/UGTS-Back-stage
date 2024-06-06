package com.ugts.post.mapper;

import java.util.List;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post createPost(CreatePostRequest request);

    PostResponse postToPostResponse(Post post);

    List<PostResponse> getAllPosts(List<Post> posts);
}
