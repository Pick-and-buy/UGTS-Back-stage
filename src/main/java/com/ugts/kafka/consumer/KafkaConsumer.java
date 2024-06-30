package com.ugts.kafka.consumer;

import com.ugts.constant.AppConstant;
import com.ugts.notification.entity.Notifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics  = AppConstant.GENERAL_NOTIFICATION_TOPIC, groupId = "notification_group")
    public void consumerGeneralMsg(Notifications notifications){
        log.info(String.format("Consuming the message from general notification::%s", notifications.toString()));
    }
}
