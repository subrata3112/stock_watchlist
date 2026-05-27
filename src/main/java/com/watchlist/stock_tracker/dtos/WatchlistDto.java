package com.watchlist.stock_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WatchlistDto {
    private long id;
    private String name;
    private long userId;
}
