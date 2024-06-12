package com.ugts.brand.mapper;

import com.ugts.brand.dto.response.NewsResponse;
import com.ugts.brand.entity.News;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsResponse toNewsResponse(News news);
}
