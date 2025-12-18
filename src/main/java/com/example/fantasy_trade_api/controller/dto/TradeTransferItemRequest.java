package com.example.fantasy_trade_api.controller.dto;

public record TradeTransferItemRequest(Long itemId, int amount,String toUid) {
    
}
