package com.ugts.comment.dto.response;

import com.ugts.post.entity.Post;
import com.ugts.user.entity.User;
import lombok.Data;

@Data
public class CommentResponseDto {
    private Long id;
    private String commentContent;
    private String user;
    private Long post;
}
