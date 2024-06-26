package com.ugts.comment.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class CommentResponseDto {
    private String id;
    private String commentContent;
    private String username;
    private String userId;
    private String postId;
    private String userImageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date createAt;
}
