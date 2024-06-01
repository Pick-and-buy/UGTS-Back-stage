package com.ugts.comment.dto.response;

import com.ugts.post.entity.Post;
import com.ugts.user.entity.User;
import lombok.Data;

@Data
public class CommentResponseDto {
    private String commentContent;
    private User user;
    private Post post;
}
