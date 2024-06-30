package com.ugts.notification.repository;

import java.util.List;

import com.ugts.notification.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, String> {
    List<Notifications> findByUserId(String userId);
}
