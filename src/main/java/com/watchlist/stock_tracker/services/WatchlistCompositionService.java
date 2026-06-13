package com.watchlist.stock_tracker.services;

import com.watchlist.stock_tracker.dtos.requests.AddStockToWatchlistRequest;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.EquityStock;
import com.watchlist.stock_tracker.models.Watchlist;
import com.watchlist.stock_tracker.models.WatchlistComposition;
import com.watchlist.stock_tracker.repositories.WatchlistCompositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistCompositionService {

    private final WatchlistCompositionRepository compositionRepository;
    private final StockService stockService;
    private final WatchlistService watchlistService;

    public void addStockToWatchlist(AddStockToWatchlistRequest request) throws IOException {

        // Validate
        Watchlist wl = watchlistService.getWatchlistById(request.getWatchlistId());
        if (wl == null) {
            throw new CustomException("Invalid Watchlist ID", 404);
        }
        if (wl.getUserId() != request.getUserId()) {
            throw new CustomException("User don't have permission", 403);
        }
        EquityStock stock = stockService.getStockById(request.getStockId());
        if (stock == null) {
            throw new CustomException("Invalid Stock ID", 404);
        }
        var existing = compositionRepository.findByWatchlistIdAndTickerId(wl.getId(), stock.getId());
        if (existing.isPresent()) {
            throw new CustomException("Stock Already in the Watchlist", 400);
        }

        // Fetch Price and Save
        var price = stockService.getStockPrice(stock.getId());
        compositionRepository.save(WatchlistComposition.builder()
                        .watchlistId(wl.getId())
                        .tickerId(stock.getId())
                        .priceDuringAddition(price.getPrice())
                        .createdAt(System.currentTimeMillis())
                .build());
    }

    public List<WatchlistComposition> getComposition(long watchlistId, long userId) {
        Watchlist watchlist = watchlistService.getWatchlistById(watchlistId);
        if (watchlist == null) {
            throw new CustomException("Invalid Watchlist Id", 400);
        }
        if (watchlist.getUserId() != userId) {
            throw new CustomException("User don't have permission", 403);
        }
        return compositionRepository.findAllByWatchlistId(watchlistId);
    }

    public void deleteComposition(long compositionId, long userId) {
        WatchlistComposition composition = compositionRepository.findById(compositionId).orElse(null);
        if (composition != null) {
            Watchlist watchlist = watchlistService.getWatchlistById(composition.getWatchlistId());
            if (watchlist != null) {
                if (watchlist.getUserId() != userId) {
                    throw new CustomException("User don't have permission", 403);
                }
            }
        }
        compositionRepository.deleteById(compositionId);
    }
}
