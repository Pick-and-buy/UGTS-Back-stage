package com.ugts.comment.service;

import com.ugts.comment.dto.CommentDto;
import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.entity.Comment;

public interface ICommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto);
    void fetchComment(Long postId);
}
