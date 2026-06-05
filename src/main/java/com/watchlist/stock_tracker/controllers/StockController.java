package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.EquityStockDto;
import com.watchlist.stock_tracker.dtos.EquityStockPriceDto;
import com.watchlist.stock_tracker.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<EquityStockDto>> getStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{stockId}/price")
    public ResponseEntity<EquityStockPriceDto> getStockPrice(@PathVariable long stockId) throws IOException {
        return ResponseEntity.ok(stockService.getStockPrice(stockId));
    }
}
