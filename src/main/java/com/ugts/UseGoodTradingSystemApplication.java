package com.ugts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableJpaRepositories
// @EnableElasticsearchRepositories(basePackages = "com.ugts.post.repository")
public class UseGoodTradingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseGoodTradingSystemApplication.class, args);
    }
}
