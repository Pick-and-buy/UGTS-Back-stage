package com.ugts.news.mapper;

import java.util.List;

import com.ugts.news.dto.response.NewsResponse;
import com.ugts.news.entity.News;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsResponse toNewsResponse(News news);

    List<NewsResponse> toAllNewsResponse(List<News> news);
}
