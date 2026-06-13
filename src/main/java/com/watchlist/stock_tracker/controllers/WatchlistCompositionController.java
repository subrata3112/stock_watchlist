package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.requests.AddStockToWatchlistRequest;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.User;
import com.watchlist.stock_tracker.models.WatchlistComposition;
import com.watchlist.stock_tracker.services.WatchlistCompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(compositionService.getComposition(watchlistId, user.getId()));
    }

    @PostMapping
    public ResponseEntity<Void> addToWatchlist(@RequestBody AddStockToWatchlistRequest request) throws IOException {
        User user = getAuthenticatedUser();
        request.setUserId(user.getId());
        compositionService.addStockToWatchlist(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{compositionId}")
    public ResponseEntity<Void> deleteStockFromComposition(@PathVariable long compositionId) {
        User user = getAuthenticatedUser();
        compositionService.deleteComposition(user.getId(), user.getId());
        return ResponseEntity.noContent().build();
    }

    private User getAuthenticatedUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(principal instanceof User)) {
                throw new CustomException("Not Properly Authenticated", 403);
            }
            return (User) principal;
        } catch (NullPointerException e) {
            throw new CustomException("Not Properly Authenticated", 403);
        }
    }

}
