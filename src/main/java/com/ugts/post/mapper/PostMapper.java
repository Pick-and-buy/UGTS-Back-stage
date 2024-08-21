package com.ugts.post.mapper;

import java.util.List;

import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostResponse postToPostResponse(Post post);

    List<PostResponse> getAllPosts(List<Post> posts);

    List<PostResponse> getRecommendedPosts(List<Post> posts);

    List<PostResponse> getFollowedPosts(List<Post> posts);
}
