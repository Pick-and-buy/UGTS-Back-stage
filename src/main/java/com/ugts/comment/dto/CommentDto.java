package com.ugts.comment.dto;

import com.ugts.post.entity.Post;
import com.ugts.user.entity.User;
import lombok.Data;

@Data
public class CommentDto {
    private String commentContent;
    private User user;
    private Post post;
}
