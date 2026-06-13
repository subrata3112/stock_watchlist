package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.WatchlistDto;
import com.watchlist.stock_tracker.dtos.requests.CreateWatchlistRequest;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.User;
import com.watchlist.stock_tracker.services.WatchlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlists")
public class WatchlistController {

    private final WatchlistService service;

    public WatchlistController(WatchlistService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<WatchlistDto>> getWatchlistsByUserId() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(this.service.getWatchlistsByUserId(user.getId()));
    }

    @PostMapping
    public ResponseEntity<WatchlistDto> createWatchlist(@RequestBody CreateWatchlistRequest request) {
        User user = getAuthenticatedUser();
        request.setUserId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.service.createWatchlist(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        this.service.deleteWatchlist(id, user.getId());
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
