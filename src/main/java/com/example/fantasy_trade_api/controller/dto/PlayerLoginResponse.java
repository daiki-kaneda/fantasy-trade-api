package com.example.fantasy_trade_api.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record PlayerLoginResponse(@NotBlank String uid, String message) {

}
