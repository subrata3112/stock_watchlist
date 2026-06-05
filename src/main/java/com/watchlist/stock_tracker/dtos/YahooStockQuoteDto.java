package com.watchlist.stock_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YahooStockQuoteDto {

    private BigDecimal regularMarketPrice;

    private BigDecimal changePercentageDay;

    private BigDecimal changePercentage52WeekLow;
}
