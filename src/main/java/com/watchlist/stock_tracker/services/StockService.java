package com.watchlist.stock_tracker.services;

import com.watchlist.stock_tracker.clients.YahooFinanceClient;
import com.watchlist.stock_tracker.dtos.EquityStockDto;
import com.watchlist.stock_tracker.dtos.EquityStockPriceDto;
import com.watchlist.stock_tracker.dtos.YahooStockQuoteDto;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.EquityStock;
import com.watchlist.stock_tracker.models.StockPrice;
import com.watchlist.stock_tracker.repositories.StockPriceRespository;
import com.watchlist.stock_tracker.repositories.StockRepository;
import com.watchlist.stock_tracker.transformers.StockPriceTransformer;
import com.watchlist.stock_tracker.transformers.StockTransformer;
import com.watchlist.stock_tracker.utils.MathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceRespository stockPriceRespository;
    private final YahooFinanceClient yahooFinanceClient;
    private final static long PRICE_EXPIRY_MILLIS = 1000*60*15;

    public List<EquityStockDto> getAllStocks() {
        var stocks = stockRepository.findAll();
        return stocks.stream()
                .map(StockTransformer::toEquityStockDto)
                .toList();
    }

    public EquityStockPriceDto getStockPrice(long stockId) throws IOException {
        var stock = getStockById(stockId);
        if (stock == null) {
            throw new CustomException("Invalid Stock Id", 404);
        }
        var dbPrice = stockPriceRespository.findByStockId(stockId).orElse(null);
        if (dbPrice == null) {
            dbPrice = saveStockPrice(stockId, fetchPriceDataFromYf(stockId));
        }
        else if (dbPrice.getPriceUpdatedAt() + PRICE_EXPIRY_MILLIS < System.currentTimeMillis()) {
            dbPrice = updateStockPrice(dbPrice, fetchPriceDataFromYf(stockId));
        }
        return StockPriceTransformer.toStockPriceDto(stock, dbPrice);
    }

    public EquityStock getStockById(long stockId) {
        if (stockId == 0) return null;
        var dbStock = stockRepository.findById(stockId);
        return dbStock.orElse(null);
    }

    private YahooStockQuoteDto fetchPriceDataFromYf(long stockId) throws IOException {
        EquityStock stock = stockRepository.findById(stockId).orElse(null);
        if (stock == null) {
            throw new CustomException("Invalid Stock Id", 404);
        }
        String yahooTicker = stock.getSymbol() + ".NS";
        return yahooFinanceClient.getStockQuote(yahooTicker);
    }

    private StockPrice saveStockPrice(long stockId, YahooStockQuoteDto stockQuote) {
        return stockPriceRespository.save(StockPrice.builder()
                        .stockId(stockId)
                        .currentPrice(MathUtil.bigDecimalToLong(stockQuote.getRegularMarketPrice()))
                        .changePercentageDay(MathUtil.bigDecimalToLong(stockQuote.getChangePercentageDay()))
                        .changePercentage52WeekLow(MathUtil.bigDecimalToLong(stockQuote.getChangePercentage52WeekLow()))
                        .priceUpdatedAt(System.currentTimeMillis())
                .build());
    }

    private StockPrice updateStockPrice(StockPrice stockPrice, YahooStockQuoteDto stockQuote) {
        stockPrice.setCurrentPrice(MathUtil.bigDecimalToLong(stockQuote.getRegularMarketPrice()));
        stockPrice.setChangePercentageDay(MathUtil.bigDecimalToLong(stockQuote.getChangePercentageDay()));
        stockPrice.setChangePercentage52WeekLow(MathUtil.bigDecimalToLong(stockQuote.getChangePercentage52WeekLow()));
        stockPrice.setPriceUpdatedAt(System.currentTimeMillis());
        return stockPriceRespository.save(stockPrice);
    }
}
