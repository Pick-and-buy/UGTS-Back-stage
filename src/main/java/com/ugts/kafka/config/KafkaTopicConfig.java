package com.ugts.kafka.config;

import com.ugts.constant.AppConstant;
//import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // General topic for all notification
//    @Bean
//    public NewTopic notificationTopic() {
//        return TopicBuilder.name(AppConstant.GENERAL_NOTIFICATION_TOPIC).build();
//    }
//
//    // topic include like, comment event
//    @Bean
//    public NewTopic postTopic() {
//        return TopicBuilder.name(AppConstant.POST_RELATED_TOPIC).build();
//    }
//
//    // topic include follow, unfollow event on a specific user
//    @Bean
//    public NewTopic followsTopic() {
//        return TopicBuilder.name(AppConstant.FOLLOW_RELATED_TOPIC).build();
//    }
//
//    // topic include buying event like sending chat, notify send package, order, etc.
//    @Bean
//    public NewTopic buyingTopic() {
//        return TopicBuilder.name(AppConstant.BUYING_RELATED_TOPIC).build();
//    }
}
