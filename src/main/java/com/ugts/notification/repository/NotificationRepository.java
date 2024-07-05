package com.ugts.notification.repository;

import java.util.List;

import com.ugts.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findByUserId(String userId);
}
