package com.micropos.order.service;

import com.micropos.order.dao.OrderMapper;
import com.micropos.order.dto.OrderDto;

import javax.annotation.Resource;
import java.util.List;

public interface OrderService {

    public Long createOrder(OrderDto orderDto);

    List<OrderDto> getAllOrders();

    OrderDto getOrder(Long id);
}
