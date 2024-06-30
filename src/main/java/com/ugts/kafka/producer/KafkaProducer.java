package com.ugts.kafka.producer;

import com.ugts.constant.AppConstant;
import com.ugts.notification.entity.Notifications;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Notifications> kafkaTemplate;

    public void sendGeneralMessage(Notifications notification) {
        // object type message
        Message<Notifications> message = MessageBuilder.withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.GENERAL_NOTIFICATION_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendPostMessage(Notifications notification) {
        // object type message
        Message<Notifications> message = MessageBuilder.withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.POST_RELATED_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendFollowMessage(Notifications notification) {
        // object type message
        Message<Notifications> message = MessageBuilder.withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.FOLLOW_RELATED_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    public void sendBuyingMessage(Notifications notification) {
        // object type message
        Message<Notifications> message = MessageBuilder.withPayload(notification)
                .setHeader(KafkaHeaders.TOPIC, AppConstant.BUYING_RELATED_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
