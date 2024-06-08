package com.ugts.comment.entity;

import com.ugts.post.entity.Post;
import com.ugts.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private String id;

    @Column(name = "comment_content")
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    Post post;
}
