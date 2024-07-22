package com.ugts.notification.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class NotificationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date timestamp;

    //    @OneToOne
    private String userToId;

    //    @OneToOne
    private String userFromId;

    private boolean delivered;

    private NotificationType notificationType;

    // To mark if the notification has been read
    private boolean isRead;

    private String userFromAvatar;

    private String postId;

    private String orderId;
}
