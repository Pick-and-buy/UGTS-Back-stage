package com.ugts.notification.mapper;

import com.ugts.notification.dto.NotificationResponse;
import com.ugts.notification.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface NotificationMapper {
    NotificationResponse toNotificationResponse(NotificationEntity notification);
}
