package com.ugts.comment.service.impl;

import com.ugts.comment.util.BadWordConfig;
import com.ugts.comment.util.Trie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CommentValidationServiceTest {
    @Mock
    private BadWordConfig badWordConfig;

    @Mock
    private Trie trie;

    @InjectMocks
    private CommentValidationServiceImpl commentValidationService;
    @Test
    void testFilterBadWords_Success() {
        String content = "This is a comment with bad words like idiot and stupid.";

        when(trie.replaceBadWords(content)).thenReturn("This is a comment with bad words like **** and *****.");

        String filteredContent = commentValidationService.filterBadWords(content);

        assertEquals("This is a comment with bad words like **** and *****.", filteredContent);

        verify(trie, times(1)).replaceBadWords(content);
        verifyNoMoreInteractions(trie);
        verifyNoInteractions(badWordConfig);
    }

    @Test
    void testFilterBadWords_ExceptionHandling() {
        String content = "This is a comment with bad words.";

        when(trie.replaceBadWords(content)).thenThrow(new RuntimeException("Error replacing bad words"));

        String filteredContent = commentValidationService.filterBadWords(content);

        assertEquals(content, filteredContent);

        verify(trie, times(1)).replaceBadWords(content);
        verifyNoMoreInteractions(trie);
        verifyNoInteractions(badWordConfig);
    }
}
