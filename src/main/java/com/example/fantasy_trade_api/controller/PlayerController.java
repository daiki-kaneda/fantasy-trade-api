package com.example.fantasy_trade_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fantasy_trade_api.controller.dto.PlayerItemDTO;
import com.example.fantasy_trade_api.controller.dto.PlayerProfileResponse;
import com.example.fantasy_trade_api.service.PlayerService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/me")
    public ResponseEntity<PlayerProfileResponse> getMyProfile(@AuthenticationPrincipal String uid) {
        PlayerProfileResponse profile = playerService.getProfileByPlayerId(uid);
        return ResponseEntity.ok()
                .body(profile);
    }

    @GetMapping("/me/items")
    public ResponseEntity<List<PlayerItemDTO>> getMyItems(@AuthenticationPrincipal String uid) {
        List<PlayerItemDTO> items = playerService.getItemsByPlayerId(uid);
        return ResponseEntity.ok()
                .body(items);
    }

}
