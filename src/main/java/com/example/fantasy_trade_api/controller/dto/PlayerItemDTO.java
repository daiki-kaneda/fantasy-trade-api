package com.example.fantasy_trade_api.controller.dto;

public record PlayerItemDTO(
        Long id,
        String name,
        Integer price,
        Integer amount
    ) {

}
