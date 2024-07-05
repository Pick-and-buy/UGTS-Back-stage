package com.ugts.notification.service;

import java.util.List;

import com.ugts.notification.dto.NotificationResponse;

public interface INotificationService {
    void notifyUser(String userId, String message, String topic);

    List<NotificationResponse> getUserNotifications(String userId);
}
