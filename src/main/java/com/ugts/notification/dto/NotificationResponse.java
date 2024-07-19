package com.ugts.notification.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.notification.entity.NotificationType;
import com.ugts.user.entity.User;
import lombok.Data;

@Data
public class NotificationResponse {
    private String notificationId;

    private String userToId;

    private String userFromId;

    private String message;

    private boolean delivered;

    private NotificationType notificationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date timestamp;

    private boolean isRead;

    private String userFromAvatar;

    private String postId;

    private String orderId;


}
