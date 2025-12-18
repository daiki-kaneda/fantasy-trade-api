package com.example.fantasy_trade_api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fantasy_trade_api.controller.dto.PlayerLoginRequest;
import com.example.fantasy_trade_api.controller.dto.PlayerLoginResponse;
import com.example.fantasy_trade_api.driver.FirebaseAuthDriver;
import com.example.fantasy_trade_api.entity.Player;
import com.example.fantasy_trade_api.repository.PlayerRepository;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PlayerRepository playerRepository;

    public PlayerLoginResponse loginOrSignUp(PlayerLoginRequest request) throws FirebaseAuthException {
        String idToken = request.idToken();
        FirebaseToken token = FirebaseAuthDriver.verifyToken(idToken);
        String uid = token.getUid();

        Optional<Player> player = playerRepository.findById(uid);

        if (player.isPresent()) {
            return new PlayerLoginResponse(
                    player.get().getUid(),
                    "Login Success!");
        } else {
            Player newPlayer = Player.create(uid, token.getName());
            playerRepository.save(newPlayer);
            return new PlayerLoginResponse(
                    newPlayer.getUid(),
                    "SignUp Success!");
        }
    }
}
