package com.ugts.notification.service;

import java.util.List;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.mapper.NotificationMapper;
import com.ugts.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getUserNotifications(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return notificationRepository.findByUserToId(userId).stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public void createNotificationStorage(NotificationEntity notificationStorage) {
        notificationRepository.save(notificationStorage);
    }

    public NotificationEntity getNotificationsByID(String id) {
        return notificationRepository
                .findByNotificationId(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
    }

    @Override
    @Transactional
    public void markNotificationAsRead(String notifyID) {
        if (notifyID == null || notifyID.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        NotificationEntity notification = getNotificationsByID(notifyID);
        if (notification.isRead()) {
            throw new AppException(ErrorCode.NOTIFICATION_IS_READ);
        }
        try {
            notification.setRead(true);
            notificationRepository.save(notification);
        } catch (AppException e) {
            log.error("An error occurred while marking notification as read: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteNotificationByUserToId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        try {
            notificationRepository.deleteNotificationEntitiesByUserToId(userId);
        } catch (Exception e) {
            log.error("An error occurred while deleting notification: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void markAllNotificationAsRead(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        try {
            List<NotificationEntity> notifications = notificationRepository.findByUserToId(userId);
            for (NotificationEntity notification : notifications) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        } catch (Exception e) {
            log.error("An error occurred while marking all notification as read: {}", e.getMessage());
        }
    }
}
