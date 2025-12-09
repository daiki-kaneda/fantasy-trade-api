package com.example.fantasy_trade_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_player_item", columnNames = { "player_uid", "item_id" })
})
public class PlayerItem extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_uid")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemMaster item;

    private Integer quantity;

    public static PlayerItem create(
            Player player,
            ItemMaster item,
            int initialQuantity) {
        PlayerItem pi = new PlayerItem();
        pi.player = player;
        pi.item = item;
        pi.quantity = initialQuantity;
        return pi;
    }

    public void addQuantity(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("マイナスの個数は追加できません。");
        this.quantity += amount;
    }

    public void consumeQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("マイナスの個数は消費できません。");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("アイテムの在庫が足りません");
        }
        this.quantity -= amount;
    }

    @Override
    public Long getId() {
        return id;
    }
}
