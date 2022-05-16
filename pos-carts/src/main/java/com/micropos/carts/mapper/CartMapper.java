package com.micropos.carts.mapper;


import com.micropos.carts.dto.CartDto;
import com.micropos.carts.model.Cart;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper {
    CartDto toCartDTO(Cart cart);
    Cart toCart(CartDto cartDTO);
}
