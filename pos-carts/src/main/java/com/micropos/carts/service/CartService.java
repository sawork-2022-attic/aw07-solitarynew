package com.micropos.carts.service;

import com.micropos.carts.model.Cart;

public interface CartService {
    public Cart add(Cart cart, String productId, int amount);
}
