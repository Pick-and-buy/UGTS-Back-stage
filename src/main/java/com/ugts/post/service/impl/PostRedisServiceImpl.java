package com.ugts.post.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.service.IPostRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostRedisServiceImpl implements IPostRedisService {
    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;

    private String getRedisKey(PageRequest pageRequest) {
        return "ALL_POSTS_" + pageRequest.getPageNumber() + "_" + pageRequest.getPageSize();
    }

    @Override
    @Scheduled(fixedRate = 3600000) // clear cache every 1 hour
    public void clear() {
        redisTemplate.delete(redisTemplate.keys("ALL_POSTS_*"));
    }

    @Override
    public List<PostResponse> getAllPosts(PageRequest pageRequest) throws JsonProcessingException {
        String key = getRedisKey(pageRequest);
        String cachedPosts = (String) redisTemplate.opsForValue().get(key);
        if (cachedPosts != null) {
            return objectMapper.readValue(cachedPosts, new TypeReference<>() {});
        }
        return null;
    }

    @Override
    public void saveAllPosts(List<PostResponse> postResponses, PageRequest pageRequest) throws JsonProcessingException {
        String key = getRedisKey(pageRequest);
        String jsonPosts = objectMapper.writeValueAsString(postResponses);
        redisTemplate.opsForValue().set(key, jsonPosts, 10, TimeUnit.MINUTES);
    }
}
