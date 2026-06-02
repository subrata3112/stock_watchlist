package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.services.StockRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StockRefreshService stockRefreshService;

    @PostMapping("/stocks/refresh")
    public ResponseEntity<Boolean> refreshStocks() {
        try {
            stockRefreshService.refreshStocks();
        } catch (IOException e) {
            throw new CustomException("Failed to refresh stocks", 500);
        }
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
