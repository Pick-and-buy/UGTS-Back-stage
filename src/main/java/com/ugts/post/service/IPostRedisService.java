package com.ugts.post.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ugts.post.dto.response.PostResponse;
import org.springframework.data.domain.PageRequest;

public interface IPostRedisService {

    void clear();

    List<PostResponse> getAllPosts(PageRequest pageRequest) throws JsonProcessingException;

    void saveAllPosts(List<PostResponse> postResponses, PageRequest pageRequest) throws JsonProcessingException;
}
