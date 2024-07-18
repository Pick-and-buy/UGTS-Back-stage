package com.ugts.kafka.producer;

import com.ugts.notification.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

//    private final KafkaTemplate<String, NotificationEntity> kafkaTemplate;

//    @Bean
//    public KafkaTemplate<String, NotificationEntity> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
    public void sendMessage(NotificationEntity notification, String topic) {
        // object type message
//        Message<NotificationEntity> message = MessageBuilder
//                .withPayload(notification)
//                .setHeader(KafkaHeaders.TOPIC, topic)
//                .build();
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        kafkaTemplate.send(message);
//        kafkaTemplate.send(topic, notification);
//        kafkaTemplate.setMessageConverter(new NotificationMessageConverter());
//        kafkaTemplate.send(topic, notification);
    }

//    public void sendPostMessage(Notifications notification) {
//        // object type message
//        Message<Notifications> message = MessageBuilder.withPayload(notification)
//                .setHeader(KafkaHeaders.TOPIC, AppConstant.POST_RELATED_TOPIC)
//                .build();
//        kafkaTemplate.send(message);
//    }
//
//    public void sendFollowMessage(Notifications notification) {
//        // object type message
//        Message<Notifications> message = MessageBuilder.withPayload(notification)
//                .setHeader(KafkaHeaders.TOPIC, AppConstant.FOLLOW_RELATED_TOPIC)
//                .build();
//        kafkaTemplate.send(message);
//    }
//
//    public void sendBuyingMessage(Notifications notification) {
//        // object type message
//        Message<Notifications> message = MessageBuilder.withPayload(notification)
//                .setHeader(KafkaHeaders.TOPIC, AppConstant.BUYING_RELATED_TOPIC)
//                .build();
//        kafkaTemplate.send(message);
//    }
}
