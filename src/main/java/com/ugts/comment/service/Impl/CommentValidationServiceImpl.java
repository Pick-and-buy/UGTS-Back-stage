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
        return trie.replaceBadWords(content);

//        List<String> badWords = badWordConfig.getBadWords();
//        for (String badWord : badWords) {
//            String regex = "(?i)" + badWord;  // Case-insensitive matching
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(content);
//
//            StringBuffer stringBuffer = new StringBuffer();
//            while (matcher.find()) {
//                String replacement = "*".repeat(matcher.group().length());
//                matcher.appendReplacement(stringBuffer, replacement);
//            }
//            matcher.appendTail(stringBuffer);
//            content = stringBuffer.toString();
//        }
//        return content;
    }
}
