package com.ugts.comment.dto.request;

import lombok.Data;

@Data
public class CommentRequestDto {
    private String commentContent;
    private String userId;
    private String postId;
}
