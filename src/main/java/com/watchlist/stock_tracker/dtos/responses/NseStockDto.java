package com.watchlist.stock_tracker.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class NseStockDto {
    private String name;

    private String industry;

    private String isin;

    private String symbol;
}
