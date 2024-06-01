package com.ugts.comment.dto.request;

import com.ugts.post.entity.Post;
import com.ugts.user.entity.User;
import lombok.Data;

@Data
public class CommentRequestDto {
    private String commentContent;
    private User user;
    private Post post;
}
