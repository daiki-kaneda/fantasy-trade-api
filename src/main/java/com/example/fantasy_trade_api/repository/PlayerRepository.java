package com.example.fantasy_trade_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fantasy_trade_api.entity.Player;

import jakarta.persistence.LockModeType;

public interface PlayerRepository extends JpaRepository<Player, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Player p WHERE p.uid=:uid")
    Optional<Player> findByIdWithLock(@Param("uid") String uid);
}
