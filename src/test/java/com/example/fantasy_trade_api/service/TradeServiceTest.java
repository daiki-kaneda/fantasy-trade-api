package com.example.fantasy_trade_api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.fantasy_trade_api.entity.ItemMaster;
import com.example.fantasy_trade_api.entity.Player;
import com.example.fantasy_trade_api.entity.PlayerItem;
import com.example.fantasy_trade_api.repository.ItemMasterRepository;
import com.example.fantasy_trade_api.repository.PlayerItemRepository;
import com.example.fantasy_trade_api.repository.PlayerRepository;

@SpringBootTest
@Transactional
public class TradeServiceTest {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerItemRepository piRepository;

    @Autowired
    private ItemMasterRepository itemRepository;

    private String playerAuid = "uid-aaa";
    private String playerBuid = "uid-bbb";
    private ItemMaster potion;
    private Long generatedPotionId;

    @BeforeEach
    void setUp() {
        Player playerA = Player.create(playerAuid, "Alice");
        Player playerB = Player.create(playerBuid, "Bob");

        playerRepository.save(playerA);
        playerRepository.save(playerB);

        potion = new ItemMaster("ポーション", 250);
        itemRepository.save(potion);
        generatedPotionId = potion.getId();

        PlayerItem pi = PlayerItem.create(playerA, potion, 10);
        piRepository.save(pi);

    }

    @Test
    @DisplayName("正常系:ゴールドが正しく送金されるかを確認")
    void testTransferGold_Success() {
        tradeService.transferGold(300, playerAuid, playerBuid);

        Player playerA = playerRepository.findById(playerAuid).orElseThrow();
        Player playerB = playerRepository.findById(playerBuid).orElseThrow();

        assertThat(playerA.getGold())
                .isEqualTo(700);
        assertThat(playerB.getGold())
                .isEqualTo(1300);
    }

    @Test
    @DisplayName("異常系:残高不足の時に、エラーが出るかを確認")
    void testTransferGold_InsufficentFunds() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.transferGold(1100, playerAuid, playerBuid));

        Player playerA = playerRepository.findById(playerAuid).orElseThrow();
        assertThat(playerA.getGold())
                .isEqualTo(1000);
    }

    @Test
    @DisplayName("正常系:アイテムが正しく転送されるかを確認")
    void testTransferItem_Success() {
        tradeService.transferItem(generatedPotionId, 3, playerAuid, playerBuid);

        PlayerItem piA = piRepository.findByPlayerIdAndItemId(playerAuid, generatedPotionId).orElseThrow();
        PlayerItem piB = piRepository.findByPlayerIdAndItemId(playerBuid, generatedPotionId).orElseThrow();

        assertThat(piA.getQuantity())
                .isEqualTo(7L);
        assertThat(piB.getQuantity())
                .isEqualTo(3L);
    }

    @Test
    @DisplayName("異常系:アイテムの在庫不足のエラーが出るかを確認")
    void testTransferItem_InvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.transferItem(
                generatedPotionId, 15, playerAuid, playerBuid));
        PlayerItem piA = piRepository.findByPlayerIdAndItemId(playerAuid, generatedPotionId).orElseThrow();
        assertThat(piA.getQuantity())
                .isEqualTo(10L);
    }

    @Test
    @DisplayName("正常系:アイテムの購入が正しく行われるのかを確認")
    void testBuyItem_Success() {
        int amount = 2;
        tradeService.buyItem(playerAuid, generatedPotionId, amount);
        Player playerA = playerRepository.findById(playerAuid).orElseThrow();
        PlayerItem piA = piRepository.findByPlayerIdAndItemId(playerAuid, generatedPotionId).orElseThrow();

        assertThat(playerA.getGold())
                .isEqualTo(1000L - amount * potion.getPrice());

        assertThat(piA.getQuantity())
                .isEqualTo(10L + 2);
    }

    @Test
    @DisplayName("異常系:アイテムの購入時に残高が足りない時にエラーが出るかを確認")
    void testBuyItem_InSufficientFunds() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.buyItem(playerAuid, generatedPotionId, 5));

        Player playerA = playerRepository.findById(playerAuid).orElseThrow();
        PlayerItem piA = piRepository.findByPlayerIdAndItemId(playerAuid, generatedPotionId).orElseThrow();

        assertThat(playerA.getGold())
                .isEqualTo(1000L);

        assertThat(piA.getQuantity())
                .isEqualTo(10L);
    }

}