package com.micropos.delivery;

import com.micropos.delivery.dto.DeliveryDto;
import com.micropos.delivery.dto.OrderDto;
import com.micropos.delivery.model.Delivery;
import com.micropos.delivery.model.Order;
import com.micropos.delivery.service.DeliveryService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.function.Consumer;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.micropos.delivery.dao")
public class DeliveryApplication {
    private static final Logger log = LoggerFactory.getLogger(DeliveryApplication.class);

    @Resource
    private StreamBridge streamBridge;

    @Resource
    DeliveryService deliveryService;

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }

//    @Bean
//    Consumer<String> sms() {
//        return log::info;
//    }

    @Bean
    Consumer<Order> create() {
        return order -> {
            DeliveryDto deliveryDto = new DeliveryDto();
            deliveryDto.setOrderId(order.getId());
            deliveryDto.setStatus(DeliveryDto.StatusEnum.CREATED);
            deliveryService.createDelivery(deliveryDto);
        };
    }
}
