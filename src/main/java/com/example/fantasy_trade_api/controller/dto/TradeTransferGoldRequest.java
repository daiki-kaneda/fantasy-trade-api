package com.example.fantasy_trade_api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TradeTransferGoldRequest(@Positive int amount, @NotBlank String toUid) {

}
