package com.ugts.notification.service;

import java.util.List;

import com.ugts.notification.dto.NotificationResponse;

public interface INotificationService {
    void sendNotification(String userId, String type, String message);

    List<NotificationResponse> getUserNotifications(String userId);
}
