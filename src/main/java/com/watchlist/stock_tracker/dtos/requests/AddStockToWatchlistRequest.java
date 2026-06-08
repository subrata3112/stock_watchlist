package com.watchlist.stock_tracker.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddStockToWatchlistRequest {

    private long watchlistId;

    private long stockId;

}
