package com.ugts.configuration;

import com.ugts.order.service.OrderService;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.post.mapper.PostMapper;
import com.ugts.post.repository.PostRepository;
import com.ugts.post.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final OrderService orderService;
    private final PostRepository postRepository;



    // TODO: add to docs
    @Scheduled(fixedRate = 86400000) // 86400000 milliseconds = 24 hours
    public void scheduleTaskWithFixedRate() {
        orderService.autoRateOrders();
    }

    @Scheduled(fixedRate = 3600000) // Every hour
    public void removeExpiredBoosts() {
        postRepository.resetExpiredBoosts(LocalDateTime.now());
    }
}
