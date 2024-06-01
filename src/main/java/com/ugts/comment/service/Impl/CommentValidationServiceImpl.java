package com.ugts.comment.service.Impl;

import com.ugts.comment.util.BadWordConfig;
import com.ugts.comment.service.ICommentValidationService;
import com.ugts.comment.util.Trie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
