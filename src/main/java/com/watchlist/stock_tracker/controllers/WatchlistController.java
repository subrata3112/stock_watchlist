package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.models.Watchlist;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WatchlistController {

    @GetMapping("/watchlists")
    public List<Watchlist> getWatchlists() {
        return List.of(new Watchlist(1));
    }
}
