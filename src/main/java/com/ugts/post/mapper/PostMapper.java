package com.ugts.post.mapper;

import com.ugts.post.dto.request.CreatePostRequest;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post createPost(CreatePostRequest request);

    PostResponse postToPostResponse(Post post);

    List<PostResponse> getAllPosts(List<Post> posts);

    List<PostResponse> getRecommendedPosts(List<Post> posts);

    List<PostResponse> getFollowedPosts(List<Post> posts);


}
