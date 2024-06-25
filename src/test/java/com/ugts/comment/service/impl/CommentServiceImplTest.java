package com.ugts.comment.service.impl;

import com.ugts.comment.dto.request.CommentRequestDto;
import com.ugts.comment.dto.response.CommentResponseDto;
import com.ugts.comment.entity.Comment;
import com.ugts.comment.mapper.CommentMapper;
import com.ugts.comment.repository.CommentRepository;
import com.ugts.comment.util.BadWordConfig;
import com.ugts.comment.util.Trie;
import com.ugts.post.entity.Post;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
@TestPropertySource("/test.properties")
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentValidationServiceImpl commentValidationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment1;

    @Mock
    private BadWordConfig badWordConfig;

    @Mock
    private Trie trie;

    @Test
    void createComment_success() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setCommentContent("Valid comment");
        requestDto.setUserId("c63bd28f-893c-420c-a2dc-a4e2982f5c66");
        requestDto.setPostId("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");

        User user = new User();
        user.setUsername("Quang");

        Post post = new Post();
        post.setId("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");
        post.setTitle("Gucci");

        Comment comment = new Comment();
        comment.setCommentContent("Valid comment");
        comment.setUser(user);
        comment.setPost(post);

        when(userRepository.findById("c63bd28f-893c-420c-a2dc-a4e2982f5c66")).thenReturn(Optional.of(user));
        when(postRepository.findById("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828")).thenReturn(Optional.of(post));
        when(commentMapper.toComment(any(CommentRequestDto.class), any(User.class), any(Post.class)))
                .thenReturn(comment);
        when(commentValidationService.filterBadWords("Valid comment")).thenReturn("Valid comment");
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentResponse(comment)).thenReturn(new CommentResponseDto());

        CommentResponseDto responseDto = commentService.createComment(requestDto);

        assertNotNull(responseDto);
        assertEquals(comment.getId(), responseDto.getId());
        verify(userRepository, times(1)).findById("c63bd28f-893c-420c-a2dc-a4e2982f5c66");
        verify(postRepository, times(1)).findById("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");
        verify(commentMapper, times(1)).toComment(requestDto, user, post);
        verify(commentValidationService, times(1)).filterBadWords("Valid comment");
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toCommentResponse(comment);
    }


    @Test
    void createComment_EmptyContent_fail() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setCommentContent("");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.createComment(requestDto);
        });
        assertEquals("Comment request must not be null", exception.getCause().getMessage());
    }

    @Test
    void createComment_UserNotFound_fail() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setCommentContent("Valid comment");
        requestDto.setUserId("c63bd28f-893c-420c-a2dc-a4e2982f5c66");
        requestDto.setPostId("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");
        when(userRepository.findById("c63bd28f-893c-420c-a2dc-a4e2982f5c66")).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.createComment(requestDto);
        });
        assertEquals("User not found", exception.getCause().getMessage());
    }

    @Test
    void createComment_PostNotFound_fail() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setCommentContent("Valid comment");
        requestDto.setUserId("c63bd28f-893c-420c-a2dc-a4e2982f5c66");
        requestDto.setPostId("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");
        User user = new User();
        user.setId("c63bd28f-893c-420c-a2dc-a4e2982f5c66");

        Post post = new Post();
        post.setId("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");
        post.setTitle("Gucci");
        when(userRepository.findById("c63bd28f-893c-420c-a2dc-a4e2982f5c66")).thenReturn(java.util.Optional.of(user));
        when(postRepository.findById("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828")).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.createComment(requestDto);
        });

        assertEquals("Post not found", exception.getCause().getMessage());
    }

    @Test
    void testGetCommentsByPostId_Success() {
        String postId = "c6fb4d0c-ec68-4b6d-9e36-566b79dd4828";
        User user = new User();
        user.setId("c63bd28f-893c-420c-a2dc-a4e2982f5c66");

        Post post = new Post();
        post.setId("c6fb4d0c-ec68-4b6d-9e36-566b79dd4828");
        Comment comment1 = new Comment();
        comment1.setId("13b1fbdf-79d5-4c48-bb8a-2ae41b3d2328");
        comment1.setCommentContent("Comment 1");
        comment1.setUser(user);
        comment1.setPost(post);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);

        when(commentRepository.findByPostId(postId)).thenReturn(comments);

        CommentResponseDto responseDto1 = new CommentResponseDto();
        responseDto1.setId(comment1.getId());
        responseDto1.setCommentContent(comment1.getCommentContent());

        when(commentMapper.toCommentResponse(comment1)).thenReturn(responseDto1);

        List<CommentResponseDto> result = commentService.getCommentsByPostId(postId);

        assertEquals(1, result.size());
        assertEquals(comment1.getId(), result.get(0).getId());
        assertEquals(comment1.getCommentContent(), result.get(0).getCommentContent());

        verify(commentRepository, times(1)).findByPostId(postId);
        verify(commentMapper, times(1)).toCommentResponse(comment1);

    }

    @Test
    void getCommentsByPostId_EmptyList() {
        String postId = "c6fb4d0c-ec68-4b6d-9e36-566b79dd4828";

        when(commentRepository.findByPostId(postId)).thenReturn(new ArrayList<>());

        List<CommentResponseDto> result = commentService.getCommentsByPostId(postId);

        assertEquals(0, result.size());

        verify(commentRepository, times(1)).findByPostId(postId);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void getCommentsByPostId() {}
}
