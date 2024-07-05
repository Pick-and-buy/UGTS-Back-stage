package com.ugts.notification.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.user.entity.User;
import lombok.Data;

@Data
public class NotificationResponse {
    private String notificationId;

    private User user;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date timestamp;

    private boolean read;
}
