package com.ugts.notification.service;

import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final NotificationRepository notificationStorageRepository;

    private List<NotificationEntity> getNotifs(String userID) {
        var notifs = notificationStorageRepository.findByUserToIdAndDeliveredFalse(userID);
        notifs.forEach(x -> x.setDelivered(true));
        notificationStorageRepository.saveAll(notifs);
        return notifs;
    }

    //TODO: add to non-UI function in report
    public Flux<ServerSentEvent<List<NotificationEntity>>> getNotificationsByUserToID(String userID) {
        if (userID != null && !userID.isBlank()) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<NotificationEntity>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(userID))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<NotificationEntity>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }
}

