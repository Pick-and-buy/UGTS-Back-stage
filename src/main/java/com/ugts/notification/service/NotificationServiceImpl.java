package com.ugts.notification.service;

import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.Notification;
import com.ugts.notification.mapper.NotificationMapper;
import com.ugts.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService{
    private final NotificationRepository notificationRepository;

//    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationMapper notificationMapper;

//    @MessageMapping("/send")
    public void sendNotification(String userId, String type, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setTimestamp(new Date());
        notification.setRead(false);
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }

    public List<NotificationResponse> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }
}
