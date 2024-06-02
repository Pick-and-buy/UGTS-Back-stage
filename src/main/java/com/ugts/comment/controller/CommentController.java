package com.ugts.comment.controller;

import com.ugts.comment.dto.CommentDto;
import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.service.ICommentService;
import com.ugts.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final ICommentService iCommentService;
    @PostMapping
    public String createComments(@RequestBody CommentRequestDto commentRequestDto) {
        var comment = iCommentService.createComment(commentRequestDto);
        return ApiResponse.<CommentResponseDto>builder().result(comment).build().toString();
    }

    //TODO: fetch comments API by post id
    //TODO: add exception
    @GetMapping("/{postId}")
    public List<CommentResponseDto> getCommentsByPostId(@PathVariable String postId) {
        return iCommentService.getCommentsByPostId(postId);
    }
}
