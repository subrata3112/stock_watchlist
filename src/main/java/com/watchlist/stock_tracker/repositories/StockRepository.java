package com.watchlist.stock_tracker.repositories;

import com.watchlist.stock_tracker.models.EquityStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<EquityStock, Long> {
    public Optional<EquityStock> findBySymbol(String symbol);
}
