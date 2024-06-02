package com.ugts.comment.service;

import org.springframework.stereotype.Service;

@Service
public interface ICommentValidationService {

    String filterBadWords(String content);
}
