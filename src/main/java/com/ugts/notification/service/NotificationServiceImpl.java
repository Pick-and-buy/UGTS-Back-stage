package com.ugts.notification.service;

import java.util.Date;
import java.util.List;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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
        System.out.println("333333333333333333");
        System.out.println(user);
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setNotificationMessage(message);
        notification.setTimestamp(new Date());
        notification.setRead(false);
        notificationRepository.save(notification);

        System.out.println("===========================================");
        System.out.println(notification);
        log.info(notification.toString());
        try {
            kafkaProducer.sendMessage(notification,topic);

        }catch (Exception e) {
            log.error("An error occurred while sending notification: " + e.getMessage());
        }
    }

        public void sendPushNotification(Long userId, String message) {
            try {
                Message msg = Message.builder()
                        .setToken(getUserDeviceToken(userId)) // Implement getUserDeviceToken method
                        .setNotification(Notification.builder()
                                .setTitle("New Notification")
                                .setBody(message)
                                .build())
                        .build();

                FirebaseMessaging.getInstance().send(msg);
            } catch (Exception e) {
                log.error("An error occurred while sending push notification: " + e.getMessage());
            }
        }

    public List<NotificationResponse> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }
    private String getUserDeviceToken(Long userId) {
        // Implement logic to retrieve the user's device token
        return "user_device_token";
    }
}
