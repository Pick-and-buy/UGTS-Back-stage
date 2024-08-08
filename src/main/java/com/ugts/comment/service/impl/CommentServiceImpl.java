package com.ugts.comment.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.entity.Comment;
import com.ugts.comment.mapper.CommentMapper;
import com.ugts.comment.repository.CommentRepository;
import com.ugts.comment.service.ICommentService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.entity.NotificationType;
import com.ugts.notification.service.NotificationServiceImpl;
import com.ugts.post.entity.Post;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentValidationServiceImpl commentValidationService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationServiceImpl notificationService;

    @Override
    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        try {
            if (Objects.equals(requestDto.getCommentContent(), "")) {
                // TODO: adding null exception
                throw new IllegalArgumentException("Comment request must not be null");
            }
            User commentUser = userRepository
                    .findById(requestDto.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            Post post = postRepository
                    .findById(requestDto.getPostId())
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
            if (Objects.equals(requestDto.getCommentContent(), "")
                    || Objects.isNull(requestDto.getUserId())
                    || Objects.isNull(requestDto.getPostId())) {
                throw new IllegalArgumentException("Comment request must have non-empty content, userId, and postId");
            }
            Comment saveComment = commentMapper.toComment(requestDto, commentUser, post);
            String filteredContent = commentValidationService.filterBadWords(requestDto.getCommentContent());
            saveComment.setCommentContent(filteredContent);
            commentRepository.save(saveComment);

            // Notify to user
            User userToNotify = post.getUser();
            if (!userToNotify.getId().equals(commentUser.getId())) {
                notificationService.createNotificationStorage(NotificationEntity.builder()
                        .delivered(false)
                        .message("Bình luận mới từ " + commentUser.getUsername() + " ✍🏻! Trả lời ngay! ")
                        .notificationType(NotificationType.COMMENT)
                        .userFromId(commentUser.getId())
                        .timestamp(new Date())
                        .userToId(userToNotify.getId())
                        .userFromAvatar(commentUser.getAvatar())
                        .postId(post.getId())
                        .build());
            }
            return commentMapper.toCommentResponse(saveComment);
        } catch (Exception e) {
            System.err.println("Exception occurred while creating comment: " + e.getMessage());
            // Handle exception as necessary, e.g., rethrow or return null
            throw new RuntimeException("Error creating comment", e);
        }
    }

    @Override
    public List<CommentResponseDto> getCommentsByPostId(String postId) {
        if (postId == null) {
            return new ArrayList<>();
        }
        return commentRepository.findByPostId(postId).stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }
}
