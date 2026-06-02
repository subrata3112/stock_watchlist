package com.watchlist.stock_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquityStockDto {

    private Long id;

    private String name;

    private String industry;

    private String isin;

    private String symbol;

    private long price;

    private long priceUpdatedAt;
}
