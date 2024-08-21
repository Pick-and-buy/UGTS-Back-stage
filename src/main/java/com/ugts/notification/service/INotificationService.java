package com.ugts.notification.service;

import java.util.List;

import com.ugts.notification.dto.NotificationResponse;

public interface INotificationService {

    List<NotificationResponse> getUserNotifications(String userId);

    void markNotificationAsRead(String notifyID);

    void deleteNotificationByUserToId(String userId);

    void markAllNotificationAsRead(String userId);
}
