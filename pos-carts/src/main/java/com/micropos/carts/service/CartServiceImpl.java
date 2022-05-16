package com.micropos.carts.service;

import com.micropos.carts.model.Cart;
import com.micropos.carts.model.Item;
import com.micropos.carts.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

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
}
