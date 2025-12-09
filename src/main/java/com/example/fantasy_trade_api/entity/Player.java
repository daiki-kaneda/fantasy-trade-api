package com.example.fantasy_trade_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Player extends BaseEntity<String> {
    @Id
    private String uid;
    private String name;
    private Long gold;

    public static Player create(
            String uid,
            String name) {
        Player player = new Player();
        player.uid = uid;
        player.name = name;
        player.gold = 1000L; // 初期の所持金
        return player;
    }

    public void addGold(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("マイナスの金額は追加できません");
        }
        this.gold += amount;
    }

    public void payGold(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("マイナスの金額は支払えません");
        }
        if (this.gold < amount) {
            throw new IllegalArgumentException("所持金が足りません.(Current: " + gold + ", Required: " + amount + ")");
        }
        this.gold -= amount;
    }

    @Override
    public String getId() {
        return uid;
    }
}
