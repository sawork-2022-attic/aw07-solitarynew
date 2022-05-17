package com.micropos.delivery.service;

import com.micropos.delivery.dto.DeliveryDto;

public interface DeliveryService {

    DeliveryDto findDeliveryByOrderId(Long orderId);
    DeliveryDto getDeliveryById(Long deliveryId);
    Long createDelivery(DeliveryDto delivery);
}
