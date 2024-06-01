package com.ugts.comment.service;

import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;

import java.util.List;

public interface ICommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto);
    List<CommentResponseDto> getCommentsByPostId(Long postId);
}
