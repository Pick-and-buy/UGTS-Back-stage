package com.ugts.comment.dto.response;

import lombok.Data;

@Data
public class CommentResponseDto {
    private String id;
    private String commentContent;
    private String username;
    private String userId;
    private String postId;
}
