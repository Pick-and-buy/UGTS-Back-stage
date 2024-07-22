package com.ugts.notification.controller;

import java.util.List;

import com.ugts.dto.ApiResponse;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.service.INotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class NotificationController {

    private final INotificationService notificationService;

    @GetMapping("/{userId}")
    public ApiResponse<List<NotificationResponse>> getUserNotifications(@PathVariable String userId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getUserNotifications(userId))
                .build();
    }

    @PatchMapping("/read/{notifyID}")
    public ApiResponse<Void> changeNotifyStatusToRead(@PathVariable String notifyID) {
        notificationService.markNotificationAsRead(notifyID);
        return ApiResponse.<Void>builder()
                .message("Success change status notification")
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> clearAllNotification(@PathVariable String userId) {
        notificationService.deleteNotificationByUserToId(userId);
        return ApiResponse.<Void>builder()
                .message("Success clear all notification")
                .build();
    }
}
