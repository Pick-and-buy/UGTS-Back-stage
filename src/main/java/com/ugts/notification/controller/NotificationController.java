package com.ugts.notification.controller;

import java.util.List;

import com.ugts.dto.ApiResponse;
import com.ugts.kafka.producer.KafkaProducer;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.mapper.NotificationMapper;
import com.ugts.notification.service.INotificationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NotificationController {
    private INotificationService notificationService;
    private NotificationMapper notificationMapper;
    private KafkaProducer kafkaProducer;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public NotificationResponse send(final NotificationEntity notifications) throws Exception {
        return notificationMapper.toNotificationResponse(notifications);
    }

//    @PostMapping("/send")
//    public ResponseEntity<String> sendNotification(@RequestBody Notifications notifications) {
//        kafkaProducer.sendMessage(notifications);
//        return ResponseEntity.ok("Notification sent successfully");
//    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<NotificationResponse>> getUserNotifications(@PathVariable String userId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getUserNotifications(userId))
                .build();
    }
}
