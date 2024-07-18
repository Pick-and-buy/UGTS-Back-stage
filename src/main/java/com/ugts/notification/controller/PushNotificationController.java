package com.ugts.notification.controller;

import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/push-notifications")
@RequiredArgsConstructor
@Slf4j
public class PushNotificationController {
    private final PushNotificationService pushNotificationService;

    @GetMapping("/{userID}")
    public Flux<ServerSentEvent<List<NotificationEntity>>> streamLastMessage(@PathVariable String userID) {
        return pushNotificationService.getNotificationsByUserToID(userID);
    }
}
