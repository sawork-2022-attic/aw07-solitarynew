package com.micropos.delivery.rest;

import com.micropos.delivery.api.DeliveryApi;
import com.micropos.delivery.dto.DeliveryDto;
import com.micropos.delivery.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api")
public class DeliveryController implements DeliveryApi {

    @Resource
    private DeliveryService deliveryService;


    @Override
    public ResponseEntity<DeliveryDto> findDeliveryByOrderId(Long orderId) {
        return new ResponseEntity<>(
                deliveryService.findDeliveryByOrderId(orderId),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<DeliveryDto> getDeliveryById(Long id) {
        return new ResponseEntity<>(
                deliveryService.getDeliveryById(id),
                HttpStatus.OK
        );
    }
}
