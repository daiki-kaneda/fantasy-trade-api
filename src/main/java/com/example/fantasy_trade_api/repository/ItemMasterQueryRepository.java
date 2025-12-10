package com.example.fantasy_trade_api.repository;

import org.springframework.data.repository.Repository;

import com.example.fantasy_trade_api.entity.ItemMaster;
import java.util.Optional;


public interface ItemMasterQueryRepository extends Repository<ItemMaster,Long>{
    Optional<ItemMaster> findById(Long id);
}
