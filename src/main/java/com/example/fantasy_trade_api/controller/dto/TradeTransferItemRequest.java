package com.example.fantasy_trade_api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TradeTransferItemRequest(@NotNull Long itemId, @Positive int amount, @NotBlank String toUid) {

}
