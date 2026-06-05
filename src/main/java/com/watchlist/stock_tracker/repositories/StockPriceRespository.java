package com.watchlist.stock_tracker.repositories;

import com.watchlist.stock_tracker.models.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockPriceRespository extends JpaRepository<StockPrice, Long> {

    Optional<StockPrice> findByStockId(long stockId);
}
