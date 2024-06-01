package com.ugts.comment.service.Impl;

import com.ugts.comment.dto.CommentDto;
import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.entity.Comment;
import com.ugts.comment.mapper.CommentMapper;
import com.ugts.comment.repository.CommentRepository;
import com.ugts.comment.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentValidationServiceImpl commentValidationService;
    @Override
    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        //TODO: create comment, check if any bad words
        Comment comment = commentMapper.toComment(requestDto);
        String filteredContent = commentValidationService.filterBadWords(comment.getCommentContent());
        comment.setCommentContent(filteredContent);
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public void fetchComment(Long postId) {

    }
}
