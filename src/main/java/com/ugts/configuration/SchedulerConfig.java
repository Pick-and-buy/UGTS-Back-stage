package com.ugts.configuration;

import com.ugts.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final OrderService orderService;

    //TODO: add to docs
    @Scheduled(fixedRate = 86400000) // 86400000 milliseconds = 24 hours
    public void scheduleTaskWithFixedRate() {
        orderService.autoRateOrders();
    }
}
