package com.example.fantasy_trade_api.config.security;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.fantasy_trade_api.driver.FirebaseAuthDriver;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Component
public class FirebaseAuthenticationProvider {
    public Authentication createAuthentication(String idToken) {
        try {
            FirebaseToken token = FirebaseAuthDriver.verifyToken(idToken);
            String uid = token.getUid();
            List<? extends GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_PLAYER"));

            return new UsernamePasswordAuthenticationToken(
                    uid, null, authorities);
        } catch (FirebaseAuthException e) {
            return null;
        }
    }
}
