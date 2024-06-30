package com.ugts.notification.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    // ID of the user to be notified
    private String userId;

    // Type of notification (like, comment, follower)
    private String typeNotification;

    private String notificationMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date notifyAt;

    // To mark if the notification has been read
    private boolean isRead;
}
