package com.example.fantasy_trade_api.controller.dto;

import jakarta.validation.constraints.NotNull;

public record PlayerLoginRequest(@NotNull String idToken) {

}
