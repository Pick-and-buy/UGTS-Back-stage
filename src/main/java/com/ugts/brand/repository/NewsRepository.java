package com.ugts.brand.repository;

import com.ugts.brand.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, String> {}
