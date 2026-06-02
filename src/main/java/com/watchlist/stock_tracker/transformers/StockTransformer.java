package com.watchlist.stock_tracker.transformers;

import com.watchlist.stock_tracker.dtos.EquityStockDto;
import com.watchlist.stock_tracker.dtos.responses.NseStockDto;
import com.watchlist.stock_tracker.models.EquityStock;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StockTransformer {

    public EquityStock toEquityStock(NseStockDto nseStock) {
        return EquityStock.builder()
                .name(nseStock.getName())
                .isin(nseStock.getIsin())
                .industry(nseStock.getIndustry())
                .symbol(nseStock.getSymbol())
                .build();
    }

    public EquityStockDto toEquityStockDto(EquityStock equityStock) {
        return EquityStockDto.builder()
                .id(equityStock.getId())
                .name(equityStock.getName())
                .industry(equityStock.getIndustry())
                .isin(equityStock.getIsin())
                .symbol(equityStock.getSymbol())
                .build();
    }
}
