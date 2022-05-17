package com.micropos.delivery.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.micropos.delivery.dao.DeliveryMapper;
import com.micropos.delivery.dto.DeliveryDto;
import com.micropos.delivery.model.Delivery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DeliveryServiceImpl implements DeliveryService {

    @Resource
    private DeliveryMapper deliveryMapper;


    @Override
    public DeliveryDto findDeliveryByOrderId(Long orderId) {
        Delivery  delivery = deliveryMapper.selectOne(new QueryWrapper<Delivery>().eq("order_id", orderId));
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setId(delivery.getId());
        deliveryDto.setOrderId(delivery.getOrderId());
        deliveryDto.setStatus(DeliveryDto.StatusEnum.valueOf(delivery.getStatus()));
        return deliveryDto;
    }

    @Override
    public DeliveryDto getDeliveryById(Long deliveryId) {
        Delivery  delivery = deliveryMapper.selectById(deliveryId);
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setId(delivery.getId());
        deliveryDto.setOrderId(delivery.getOrderId());
        deliveryDto.setStatus(DeliveryDto.StatusEnum.valueOf(delivery.getStatus()));
        return deliveryDto;
    }

    @Override
    public Long createDelivery(DeliveryDto delivery) {
        Delivery delivery1 = new Delivery();
        delivery1.setOrderId(delivery.getOrderId());
        delivery1.setStatus(delivery.getStatus().name());
        deliveryMapper.insert(delivery1);
        return delivery1.getId();
    }


}
