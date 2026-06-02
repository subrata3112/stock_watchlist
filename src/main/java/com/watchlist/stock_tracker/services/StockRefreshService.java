package com.watchlist.stock_tracker.services;

import com.watchlist.stock_tracker.dtos.responses.NseStockDto;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.repositories.StockRepository;
import com.watchlist.stock_tracker.transformers.StockTransformer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockRefreshService {

    private final RestClient restClient;
    private final StockRepository stockRepository;
    private static final String NIFTY_500_URL = "https://www.niftyindices.com/IndexConstituent/ind_nifty500list.csv";

    // This method will refresh the stock list
    public Void refreshStocks() throws IOException {
        String resp = restClient.get()
                .uri(NIFTY_500_URL)
                .retrieve()
                .body(String.class);

        if (resp == null) {
            throw new CustomException("Failed to fetch Stocks from NSE", 500);
        }

        StringReader reader = new StringReader(resp);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                .setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build());

        List<NseStockDto> nseStocks = new ArrayList<>();
        for (CSVRecord record: parser) {
            var nseStock = NseStockDto.builder()
                    .name(record.get("Company Name"))
                    .industry(record.get("Industry"))
                    .isin(record.get("ISIN Code"))
                    .symbol(record.get("Symbol"))
                    .build();
            nseStocks.add(nseStock);
        }

        updateStocksToTable(nseStocks);
        return null;
    }

    private void updateStocksToTable(List<NseStockDto> nseStocks) {
        nseStocks.forEach(nseStock -> {
            var dbStock = stockRepository.findBySymbol(nseStock.getSymbol());
            if (dbStock.isEmpty()) {
                stockRepository.save(StockTransformer.toEquityStock(nseStock));
            }
        });
    }
}
