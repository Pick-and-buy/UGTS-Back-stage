package com.ugts.notification.service;

import java.util.List;

import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;

public interface INotificationService {
    void notifyUser(String userId, String message, String topic);

    List<NotificationResponse> getUserNotifications(String userId);

    void changeNotifyStatusToRead(String notifyID);

    List<NotificationEntity>getNotificationsByUserID(String userId);
}
