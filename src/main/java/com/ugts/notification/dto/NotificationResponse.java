package com.ugts.notification.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class NotificationResponse {
    private String notificationId;
    private String userId;
    private String type;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date timestamp;

    private boolean read;
}
