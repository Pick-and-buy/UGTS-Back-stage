package com.ugts.kafka.producer;

import com.ugts.constant.AppConstant;
import com.ugts.notification.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, NotificationEntity> kafkaTemplate;

    public void sendGeneralMessage(NotificationEntity notification){
        //object type message
        Message<NotificationEntity> message = MessageBuilder
                .withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.GENERAL_NOTIFICATION_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendPostMessage(NotificationEntity notification){
        //object type message
        Message<NotificationEntity> message = MessageBuilder
                .withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.POST_RELATED_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendFollowMessage(NotificationEntity notification){
        //object type message
        Message<NotificationEntity> message = MessageBuilder
                .withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.FOLLOW_RELATED_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendBuyingMessage(NotificationEntity notification){
        //object type message
        Message<NotificationEntity> message = MessageBuilder
                .withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.BUYING_RELATED_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
