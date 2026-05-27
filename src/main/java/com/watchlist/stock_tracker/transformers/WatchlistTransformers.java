package com.watchlist.stock_tracker.transformers;

import com.watchlist.stock_tracker.dtos.WatchlistDto;
import com.watchlist.stock_tracker.models.Watchlist;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WatchlistTransformers {

    public WatchlistDto getWatchlistResponse(Watchlist watchlist) {
        return WatchlistDto.builder()
                .id(watchlist.getId())
                .name(watchlist.getName())
                .userId(watchlist.getId())
                .build();
    }
}
