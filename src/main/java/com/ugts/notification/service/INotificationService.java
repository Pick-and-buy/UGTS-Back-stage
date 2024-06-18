package com.ugts.notification.service;

import com.ugts.notification.dto.NotificationResponse;

import java.util.List;

public interface INotificationService {
    void sendNotification(String userId, String type, String message);
     List<NotificationResponse> getUserNotifications(String userId);
}
