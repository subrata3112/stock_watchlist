package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.WatchlistDto;
import com.watchlist.stock_tracker.dtos.requests.CreateWatchlistRequest;
import com.watchlist.stock_tracker.services.WatchlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlists")
public class WatchlistController {

    private final WatchlistService service;

    public WatchlistController(WatchlistService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WatchlistDto>> getWatchlistsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(this.service.getWatchlistsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<WatchlistDto> createWatchlist(@RequestBody CreateWatchlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.service.createWatchlist(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable Long id) {
        this.service.deleteWatchlist(id);
        return ResponseEntity.noContent().build();
    }

}
