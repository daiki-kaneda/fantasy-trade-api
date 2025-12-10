package com.example.fantasy_trade_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fantasy_trade_api.entity.ItemMaster;
import com.example.fantasy_trade_api.entity.Player;
import com.example.fantasy_trade_api.entity.PlayerItem;
import com.example.fantasy_trade_api.repository.ItemMasterQueryRepository;
import com.example.fantasy_trade_api.repository.PlayerItemRepository;
import com.example.fantasy_trade_api.repository.PlayerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {
    private final PlayerRepository playerRepository;
    private final PlayerItemRepository piRepository;
    private final ItemMasterQueryRepository itemMasterQueryRepository;

    @Transactional // ロックはトランザクション内でのみ有効
    public void transferGold(int amount, String fromUid, String toUid) {
        if (fromUid.equals(toUid)) {
            throw new IllegalArgumentException("自分自身に送金することはできません");
        }

        String firstUid;
        String secondUid;
        if (fromUid.compareTo(toUid) < 0) {
            firstUid = fromUid;
            secondUid = toUid;
        } else {
            firstUid = toUid;
            secondUid = fromUid;
        }

        // デッドロック回避のために順序を決めてロックしています
        Player firstPlayer = playerRepository.findByIdWithLock(firstUid)
                .orElseThrow(() -> new EntityNotFoundException());
        Player secondPlayer = playerRepository.findByIdWithLock(secondUid)
                .orElseThrow(() -> new EntityNotFoundException());

        boolean firstIsSender = firstPlayer.getUid().equals(fromUid);
        Player sender = firstIsSender ? firstPlayer : secondPlayer;
        Player receiver = firstIsSender ? secondPlayer : firstPlayer;

        sender.payGold(amount); // もし残高が足りなければエラーが出ます
        receiver.addGold(amount);

        playerRepository.save(sender);
        playerRepository.save(receiver);
    }

    @Transactional
    public void transferItem(Long itemId, int amount, String fromUid, String toUid) {
        if (fromUid.equals(toUid)) {
            throw new IllegalArgumentException("自分自身にアイテムを贈ることは出来ません。");
        }

        if (piRepository.findByPlayerIdAndItemId(toUid, itemId).isEmpty()) {
            addEmptyPlayerItem(itemId, toUid);
        }

        String firstUid;
        String secondUid;

        if (fromUid.compareTo(toUid) < 0) {
            firstUid = fromUid;
            secondUid = toUid;
        } else {
            firstUid = toUid;
            secondUid = fromUid;
        }

        // デッドロック回避のために順序を決めてロックしています
        PlayerItem firstPi = piRepository.findByPlayerIdAndItemIdWithLock(firstUid, itemId)
                .orElseThrow(() -> new EntityNotFoundException());
        PlayerItem secondPi = piRepository.findByPlayerIdAndItemIdWithLock(secondUid, itemId)
                .orElseThrow(() -> new EntityNotFoundException());

        boolean firstIsSender = firstPi.getPlayer().getUid().equals(fromUid);

        PlayerItem senderPi = firstIsSender ? firstPi : secondPi;
        PlayerItem receiverPi = firstIsSender ? secondPi : firstPi;

        senderPi.consumeQuantity(amount);
        receiverPi.addQuantity(amount);

        piRepository.save(senderPi);
        piRepository.save(receiverPi);
    }

    @Transactional
    private void addEmptyPlayerItem(
            Long itemId, String uid) {
        Player player = playerRepository.findById(uid).orElseThrow(() -> new EntityNotFoundException());
        ItemMaster item = itemMasterQueryRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException());
        PlayerItem newPi = PlayerItem.create(player, item, 0);
        piRepository.save(newPi);

    }

    @Transactional
    public void buyItem(String uid, Long itemId, int amount) {
        if (piRepository.findByPlayerIdAndItemId(uid, itemId).isEmpty()) {
            addEmptyPlayerItem(itemId, uid);
        }

        Player player = playerRepository.findByIdWithLock(uid)
                .orElseThrow(() -> new EntityNotFoundException());
        PlayerItem pi = piRepository.findByPlayerIdAndItemIdWithLock(
                uid, itemId).orElseThrow(() -> new EntityNotFoundException());
        ItemMaster item = itemMasterQueryRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException());

        int totalCost = item.getPrice() * amount;
        player.payGold(totalCost);
        pi.addQuantity(amount);

        playerRepository.save(player);
        piRepository.save(pi);
    }
}
