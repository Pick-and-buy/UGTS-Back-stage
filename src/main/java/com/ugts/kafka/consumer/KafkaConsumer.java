package com.ugts.kafka.consumer;

import com.ugts.constant.AppConstant;
import com.ugts.notification.entity.NotificationEntity;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
//    @KafkaListener(topics  = AppConstant.GENERAL_NOTIFICATION_TOPIC, groupId = "notification_group")
    public void consumerGeneralMsg(NotificationEntity notifications){
        //TODO: send notification with firebase

        log.info(String.format("Consuming the message from general notification::%s", notifications.toString()));
    }

//    @KafkaListener(topics  = AppConstant.POST_RELATED_TOPIC, groupId = "notification_group")
    public void consumerPostMsg(NotificationEntity notifications){
        //TODO: send notification with firebase
        System.out.println("Consuming the message from post notification::" + notifications.toString());
//        log.info(String.format("Consuming the message from post notification::%s", notifications.toString()));
    }
//@KafkaListener(topics  = AppConstant.POST_RELATED_TOPIC, groupId = "notification_group")
//public void consumerPostMsg(String message){
//    //TODO: send notification with firebase
//    System.out.println("Consuming the message from post notification::" + message);
////    log.info(String.format("Consuming the message from post notification::%s", message));
//}

//    @KafkaListener(topics  = AppConstant.BUYING_RELATED_TOPIC, groupId = "notification_group")
    public void consumerBuyingMsg(NotificationEntity notifications){
        //TODO: send notification with firebase

        log.info(String.format("Consuming the message from buying related notification::%s", notifications.toString()));
    }

//    @KafkaListener(topics  = AppConstant.FOLLOW_RELATED_TOPIC, groupId = "notification_group")
    public void consumerFollowMsg(NotificationEntity notifications){
        //TODO: send notification with firebase

        log.info(String.format("Consuming the message from follow related notification::%s", notifications.toString()));
    }


}
