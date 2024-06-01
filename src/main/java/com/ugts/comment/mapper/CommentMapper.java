package com.ugts.comment.mapper;

import com.ugts.comment.dto.CommentDto;
import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDto toCommentResponse(Comment comment);

    Comment toComment(CommentRequestDto commentRequestDto);
}
