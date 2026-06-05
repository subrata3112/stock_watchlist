package com.watchlist.stock_tracker.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
public class EquityStockPriceDto extends EquityStockDto {

    private long stockId;

    private long price;

    private long changePercentageDay;

    private long changePercentage52WeekLow;

    private long priceUpdatedAt;
}
