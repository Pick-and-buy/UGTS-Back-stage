package com.ugts.common.configuration;

import java.time.LocalDateTime;

import com.ugts.order.service.OrderService;
import com.ugts.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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
