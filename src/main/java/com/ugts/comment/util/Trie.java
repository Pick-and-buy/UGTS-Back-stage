package com.ugts.comment.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(ch, c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    public boolean containsBadWord(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }

    public void buildTrie(List<String> words) {
        for (String word : words) {
            insert(word);
        }
    }

    public String replaceBadWords(String content) {
        StringBuilder result = new StringBuilder();
        TrieNode current = root;
        int start = 0;
        int i = 0;
        while (i < content.length()) {
            char ch = content.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                result.append(content.charAt(start));
                start++;
                i = start;
                current = root;
            } else {
                current = node;
                if (current.isEndOfWord()) {
                    result.append("*".repeat(i - start + 1));
                    start = i + 1;
                    current = root;
                }
                i++;
            }
        }
        result.append(content.substring(start));
        return result.toString();
    }
}

