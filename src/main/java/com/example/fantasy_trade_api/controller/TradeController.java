package com.example.fantasy_trade_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fantasy_trade_api.controller.dto.TradeBuyItemRequest;
import com.example.fantasy_trade_api.controller.dto.TradeTransferGoldRequest;
import com.example.fantasy_trade_api.controller.dto.TradeTransferItemRequest;
import com.example.fantasy_trade_api.service.TradeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @PostMapping("/transfer-gold")
    public ResponseEntity<Void> transferGold(
            @AuthenticationPrincipal String fromUid,
            @RequestBody TradeTransferGoldRequest request) {
        tradeService.transferGold(request.amount(), fromUid, request.toUid());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer-item")
    public ResponseEntity<Void> transferItem(
            @AuthenticationPrincipal String fromUid,
            @RequestBody TradeTransferItemRequest request) {
        tradeService.transferItem(request.itemId(), request.amount(), fromUid, request.toUid());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/buy-item")
    public ResponseEntity<Void> buyItem(
            @AuthenticationPrincipal String uid,
            @RequestBody TradeBuyItemRequest request) {
        tradeService.buyItem(uid, request.itemId(), request.amount());
        return ResponseEntity.ok().build();
    }
}
