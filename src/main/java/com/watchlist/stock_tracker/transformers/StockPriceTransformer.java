package com.watchlist.stock_tracker.transformers;

import com.watchlist.stock_tracker.dtos.EquityStockDto;
import com.watchlist.stock_tracker.dtos.EquityStockPriceDto;
import com.watchlist.stock_tracker.models.EquityStock;
import com.watchlist.stock_tracker.models.StockPrice;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StockPriceTransformer {

    public EquityStockPriceDto toStockPriceDto(EquityStock equityStock, StockPrice price) {
        return EquityStockPriceDto.builder()
                .stockId(equityStock.getId())
                .name(equityStock.getName())
                .industry(equityStock.getIndustry())
                .isin(equityStock.getIsin())
                .symbol(equityStock.getSymbol())
                .price(price.getCurrentPrice())
                .changePercentageDay(price.getChangePercentageDay())
                .changePercentage52WeekLow(price.getChangePercentage52WeekLow())
                .priceUpdatedAt(price.getPriceUpdatedAt())
                .build();
    }
}
