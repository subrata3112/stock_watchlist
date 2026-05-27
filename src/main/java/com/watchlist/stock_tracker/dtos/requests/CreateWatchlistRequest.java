package com.watchlist.stock_tracker.dtos.requests;

import lombok.Data;

@Data
public class CreateWatchlistRequest {

    private long userId;

    private String name;
}
