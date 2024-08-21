package com.ugts.comment.mapper;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.entity.Comment;
import com.ugts.post.entity.Post;
import com.ugts.user.entity.User;
import org.springframework.stereotype.Component;

// @Mapper(componentModel = "spring")
@Component
public class CommentMapper {

    public CommentResponseDto toCommentResponse(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setCommentContent(comment.getCommentContent());
        commentResponseDto.setUsername(
                Optional.ofNullable(comment.getUser()).map(User::getUsername).orElse(null));
        commentResponseDto.setUserId(comment.getUser().getId());
        commentResponseDto.setPostId(Objects.requireNonNull(
                Optional.ofNullable(comment.getPost()).map(Post::getId).orElse(null)));
        commentResponseDto.setCreateAt(comment.getCreateAt());
        commentResponseDto.setUserImageUrl(comment.getUserImageUrl());
        return commentResponseDto;
    }

    public Comment toComment(CommentRequestDto commentRequestDto, User user, Post post) {
        Comment comment = new Comment();
        comment.setCommentContent(commentRequestDto.getCommentContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreateAt(new Date());
        comment.setUserImageUrl(user.getAvatar());
        return comment;
    }
}
