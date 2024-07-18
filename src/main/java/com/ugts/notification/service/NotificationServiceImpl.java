package com.ugts.notification.service;

import java.util.Date;
import java.util.List;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.kafka.producer.KafkaProducer;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.mapper.NotificationMapper;
import com.ugts.notification.repository.NotificationRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final KafkaProducer kafkaProducer;

    private final UserRepository userRepository;

    public void notifyUser(String userId, String message, String topic) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        NotificationEntity notification = new NotificationEntity();
        notification.setUserToId(user.getId());
        notification.setMessage(message);
        notification.setTimestamp(new Date());
        notification.setRead(false);
        notificationRepository.save(notification);
        try {
            kafkaProducer.sendMessage(notification,topic);
        }catch (Exception e) {
            log.error("An error occurred while sending notification: " + e.getMessage());
        }
    }

    @Override
    public List<NotificationResponse> getUserNotifications(String userId) {
//        System.out.println(notificationRepository.findByUserToId(userId));
        return notificationRepository.findByUserToId(userId).stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public void createNotificationStorage(NotificationEntity notificationStorage) {
        notificationRepository.save(notificationStorage);
    }

    public NotificationEntity getNotificationsByID(String id) {
        return notificationRepository.findByNotificationId(id).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
    }

    public List<NotificationResponse> getNotificationsByUserIDNotRead(String userID) {
        return notificationRepository.findByUserToIdAndDeliveredFalse(userID).stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
//        return notificationRepository.findByUserToIdAndDeliveredFalse(userID);
    }

    public List<NotificationEntity> getNotificationsByUserID(String userID) {
        return notificationRepository.findByUserToId(userID);
    }

    @Override
    public void changeNotifyStatusToRead(String notifyID) {
        NotificationEntity notification = getNotificationsByID(notifyID);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void clear() {
        notificationRepository.deleteAll();
    }
}
