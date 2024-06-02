package com.ugts.comment.service;

import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ICommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto);
    List<CommentResponseDto> getCommentsByPostId(String postId);
}
