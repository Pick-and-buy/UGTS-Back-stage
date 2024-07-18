package com.ugts.notification.repository;

import java.util.List;
import java.util.Optional;

import com.ugts.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
//    List<NotificationEntity> findByUserId(String userId);

    Optional<NotificationEntity> findByNotificationId(String id);

    List<NotificationEntity> findByUserToIdAndDeliveredFalse(String id);

    List<NotificationEntity> findByUserToId(String id);
}
