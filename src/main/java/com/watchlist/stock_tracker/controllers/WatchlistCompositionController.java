package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.requests.AddStockToWatchlistRequest;
import com.watchlist.stock_tracker.models.WatchlistComposition;
import com.watchlist.stock_tracker.services.WatchlistCompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/composition")
@RequiredArgsConstructor
public class WatchlistCompositionController {

    private final WatchlistCompositionService compositionService;

    @GetMapping("/{watchlistId}")
    public ResponseEntity<List<WatchlistComposition>> getWatchlistComposition(@PathVariable long watchlistId) {
        return ResponseEntity.ok(compositionService.getComposition(watchlistId));
    }

    @PostMapping
    public ResponseEntity<Void> addToWatchlist(@RequestBody AddStockToWatchlistRequest request) throws IOException {
        compositionService.addStockToWatchlist(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{compositionId}")
    public ResponseEntity<Void> deleteStockFromComposition(@PathVariable long compositionId) {
        compositionService.deleteComposition(compositionId);
        return ResponseEntity.noContent().build();
    }

}
