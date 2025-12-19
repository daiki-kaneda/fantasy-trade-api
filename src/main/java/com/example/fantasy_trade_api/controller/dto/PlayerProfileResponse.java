package com.example.fantasy_trade_api.controller.dto;

public record PlayerProfileResponse(
        String uid,
        String name,
        Long gold) {

}
