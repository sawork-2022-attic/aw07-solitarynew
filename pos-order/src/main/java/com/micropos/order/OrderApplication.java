package com.micropos.order;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.micropos.order.dao")
public class OrderApplication {
    private static final Logger log = LoggerFactory.getLogger(OrderApplication.class);

//    @Resource
//    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

//    @Bean
//    Supplier<String> sms() {
//        return () -> {
//            log.info("send");
//            streamBridge.send("sms-in-0", "send");
//            return "send";
//        };
//    }
}
