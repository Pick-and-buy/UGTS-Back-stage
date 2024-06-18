package com.ugts.notification.mapper;

import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}
