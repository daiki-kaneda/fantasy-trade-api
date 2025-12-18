package com.example.fantasy_trade_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fantasy_trade_api.controller.dto.PlayerLoginRequest;
import com.example.fantasy_trade_api.controller.dto.PlayerLoginResponse;
import com.example.fantasy_trade_api.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public PlayerLoginResponse login(@RequestBody PlayerLoginRequest request) throws FirebaseAuthException{
        return authService.loginOrSignUp(request);
    }
    
}
