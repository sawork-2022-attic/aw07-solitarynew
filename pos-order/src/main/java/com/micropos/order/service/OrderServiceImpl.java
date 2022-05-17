package com.micropos.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropos.order.dao.OrderMapper;
import com.micropos.order.dto.CartDto;
import com.micropos.order.dto.OrderDto;
import com.micropos.order.model.Order;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StreamBridge streamBridge;

    @Override
    public Long createOrder(OrderDto orderDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String cart = null;
        try {
             cart = objectMapper.writeValueAsString(orderDto.getCart());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Order order = new Order();
        order.setTotal(orderDto.getTotal());
        order.setCart(cart.getBytes(StandardCharsets.UTF_8));
        orderMapper.insert(order);

        streamBridge.send("create-in-0", order);
        return order.getId();
    }

    @Override
    public List<OrderDto> getAllOrders() {
        ObjectMapper objectMapper = new ObjectMapper();
        return orderMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(order -> {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setId(order.getId());
                    orderDto.setTotal(order.getTotal());
                    try {
                        orderDto.setCart(objectMapper.readValue(
                                new StringReader(new String(order.getCart(), StandardCharsets.UTF_8)),
                                CartDto.class));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return orderDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrder(Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = orderMapper.selectById(id);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTotal(order.getTotal());
        try {
            orderDto.setCart(objectMapper.readValue(
                    new StringReader(new String(order.getCart(), StandardCharsets.UTF_8)),
                    CartDto.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return orderDto;
    }
}
