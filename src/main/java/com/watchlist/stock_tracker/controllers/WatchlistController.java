package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.WatchlistDto;
import com.watchlist.stock_tracker.dtos.requests.CreateWatchlistRequest;
import com.watchlist.stock_tracker.services.WatchlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WatchlistController {

    private final WatchlistService service;

    public WatchlistController(WatchlistService service) {
        this.service = service;
    }

    @GetMapping("/watchlists/{userId}")
    public ResponseEntity<List<WatchlistDto>> getWatchlistsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(this.service.getWatchlistsByUserId(userId));
    }

    @PostMapping("/watchlists")
    public ResponseEntity<WatchlistDto> createWatchlist(@RequestBody CreateWatchlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.service.createWatchlist(request));
    }

}
