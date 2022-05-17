package com.micropos.order.rest;


import com.micropos.order.api.OrderApi;
import com.micropos.order.dto.OrderDto;
import com.micropos.order.service.OrderService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api")
public class OrderController implements OrderApi {

//    @Resource
//    private Supplier<String> send;

    @Resource
    private StreamBridge streamBridge;

    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    void test(){
        streamBridge.send("sms-in-0", "test");
    }

    @Override
    public ResponseEntity<Long> createOrder(OrderDto orderDto) {
        return new ResponseEntity<>(
                orderService.createOrder(orderDto),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(
                orderService.getAllOrders(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<OrderDto> getOrder(Long id) {
        return new ResponseEntity<>(
                orderService.getOrder(id),
                HttpStatus.OK
        );
    }
}
