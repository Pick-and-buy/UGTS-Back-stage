package com.ugts.news.repository;

import com.ugts.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, String> {}
