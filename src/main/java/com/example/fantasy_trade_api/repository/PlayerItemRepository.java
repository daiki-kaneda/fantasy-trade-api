package com.example.fantasy_trade_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.fantasy_trade_api.entity.PlayerItem;

import jakarta.persistence.LockModeType;

public interface PlayerItemRepository extends JpaRepository<PlayerItem, Long> {
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT pi FROM PlayerItem pi WHERE pi.player.uid = :uid AND pi.item.id = :itemId")
        public Optional<PlayerItem> findByPlayerIdAndItemIdWithLock(
                        @Param("uid") String uid,
                        @Param("itemId") Long itemId);

        @Query("SELECT pi FROM PlayerItem pi WHERE pi.player.uid = :uid AND pi.item.id = :itemId")
        public Optional<PlayerItem> findByPlayerIdAndItemId(
                        @Param("uid") String uid,
                        @Param("itemId") Long itemId);

        @Query("SELECT pi FROM PlayerItem pi " +
                        "JOIN FETCH pi.item " +
                        "WHERE pi.player.uid = :uid")
        public List<PlayerItem> findByPlayerId(@Param("uid") String uid);
}