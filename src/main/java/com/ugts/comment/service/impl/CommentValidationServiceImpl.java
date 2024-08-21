package com.ugts.comment.service.impl;

import com.ugts.comment.service.ICommentValidationService;
import com.ugts.comment.util.BadWordConfig;
import com.ugts.comment.util.Trie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentValidationServiceImpl implements ICommentValidationService {
    private final BadWordConfig badWordConfig;
    private final Trie trie;

    @Override
    public String filterBadWords(String content) {
        try {
            return trie.replaceBadWords(content);
        } catch (Exception e) {
            // Log the exception
            System.err.println("Exception occurred while filtering bad words: " + e.getMessage());
            // Return the original content if an error occurs
            return content;
        }
    }
}
