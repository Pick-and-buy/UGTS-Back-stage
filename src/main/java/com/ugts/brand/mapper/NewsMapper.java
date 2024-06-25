package com.ugts.brand.mapper;

import com.ugts.brand.dto.response.NewsResponse;
import com.ugts.brand.entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsResponse toNewsResponse(News news);

    List<NewsResponse> toAllNewsResponse(List<News> news);
}
