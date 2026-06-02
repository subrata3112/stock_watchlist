package com.watchlist.stock_tracker.services;

import com.watchlist.stock_tracker.dtos.EquityStockDto;
import com.watchlist.stock_tracker.repositories.StockRepository;
import com.watchlist.stock_tracker.transformers.StockTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<EquityStockDto> getAllStocks() {
        var stocks = stockRepository.findAll();
        return stocks.stream()
                .map(StockTransformer::toEquityStockDto)
                .toList();
    }
}
