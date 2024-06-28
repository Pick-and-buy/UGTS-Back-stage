package com.ugts.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;
import com.google.firebase.messaging.Notification;
import com.ugts.notification.mapper.NotificationMapper;
import com.ugts.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService{
    private final NotificationRepository notificationRepository;

//    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationMapper notificationMapper;

//    @MessageMapping("/send")
    public void sendNotification(String userId, String type, String message) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setTimestamp(new Date());
        notification.setRead(false);
        notificationRepository.save(notification);

//        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
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
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    private String getUserDeviceToken(Long userId) {
        // Implement logic to retrieve the user's device token
        return "user_device_token";
    }
}
