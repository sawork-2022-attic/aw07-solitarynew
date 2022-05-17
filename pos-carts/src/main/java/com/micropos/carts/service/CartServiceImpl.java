package com.micropos.carts.service;

import com.micropos.carts.dto.CartDto;
import com.micropos.carts.dto.OrderDto;
import com.micropos.carts.mapper.CartMapper;
import com.micropos.carts.model.Cart;
import com.micropos.carts.model.Item;
import com.micropos.carts.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Resource
    private CartMapper cartMapper;

    @Override
    public Cart add(Cart cart, String productId, int amount) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> {
            ResponseEntity<Product> productResponseEntity = restTemplate.
                    getForEntity("http://pos-products/api/products/" + productId, Product.class);
            //Product product = new Product(productId, "", 0, "");
            Product product = productResponseEntity.getBody();
            if (product == null) return cart;
            cart.addItem(new Item(product, amount));
            return cart;
        }, ex -> {
            cart.addItem(new Item(new Product(productId, "服务器出故障了，只能这样提醒你了", 0, ""), amount));
            return cart;
        });
    }

    @Override
    public Long submit(Cart cart) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> {
            CartDto cartDto = cartMapper.toCartDTO(cart);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<CartDto> httpEntity1 = new HttpEntity<>(cartDto, headers);
            ResponseEntity<BigDecimal> bigDecimalResponseEntity = restTemplate
                    .postForEntity("http://pos-counter/api/counter/checkout", httpEntity1, BigDecimal.class);
            BigDecimal total = bigDecimalResponseEntity.getBody();

            OrderDto orderDto = new OrderDto();
            orderDto.setTotal(total);
            orderDto.setCart(cartDto);
            HttpEntity<OrderDto> httpEntity2 = new HttpEntity<>(orderDto, headers);
            ResponseEntity<Long> orderResponseEntity = restTemplate
                    .postForEntity("http://pos-order/api/order", httpEntity2, Long.class);

            cart.getItems().clear();
            return orderResponseEntity.getBody();
        }, ex -> {
            log.info(ex.getMessage());
            return Long.valueOf(-1);
        });
    }
}
