package com.watchlist.stock_tracker.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class WatchlistComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long tickerId;

    private long watchlistId;
}
