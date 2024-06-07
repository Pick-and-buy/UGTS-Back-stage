package com.ugts.comment.service;

import java.util.List;

import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;

public interface ICommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> getCommentsByPostId(String postId);
}
