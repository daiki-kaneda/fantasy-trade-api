package com.example.fantasy_trade_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fantasy_trade_api.controller.dto.PlayerItemDTO;
import com.example.fantasy_trade_api.controller.dto.PlayerProfileResponse;
import com.example.fantasy_trade_api.entity.ItemMaster;
import com.example.fantasy_trade_api.entity.Player;
import com.example.fantasy_trade_api.entity.PlayerItem;
import com.example.fantasy_trade_api.repository.PlayerItemRepository;
import com.example.fantasy_trade_api.repository.PlayerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerItemRepository piRepository;

    public PlayerProfileResponse getProfileByPlayerId(
            String uid) {
        Player player = playerRepository.findById(uid).orElseThrow(
            ()->new EntityNotFoundException("Player not found with uid:"+uid)
        );
        return new PlayerProfileResponse(player.getUid(), player.getName(), player.getGold());
    }

    public List<PlayerItemDTO> getItemsByPlayerId(
            String uid) {
        List<PlayerItem> pis = piRepository.findByPlayerId(uid);
        return pis
                .stream()
                .map(pi -> {
                    ItemMaster item = pi.getItem();
                    return new PlayerItemDTO(item.getId(), item.getName(), item.getPrice(), pi.getQuantity());
                }).toList();

    }
}
