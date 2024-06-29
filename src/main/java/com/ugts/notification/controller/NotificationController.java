package com.ugts.notification.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.kafka.producer.KafkaProducer;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.mapper.NotificationMapper;
import com.ugts.notification.service.INotificationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public NotificationResponse send(final NotificationEntity notificationEntity) throws Exception {
        return notificationMapper.toNotificationResponse(notificationEntity);
    }
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationEntity notificationEntity) {
        kafkaProducer.sendGeneralMessage(notificationEntity);
        return ResponseEntity.ok("Notification sent successfully");
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<NotificationResponse>> getUserNotifications(@PathVariable String userId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getUserNotifications(userId))
                .build();
    }
}
