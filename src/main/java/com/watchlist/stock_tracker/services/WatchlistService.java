package com.watchlist.stock_tracker.services;

import com.watchlist.stock_tracker.dtos.WatchlistDto;
import com.watchlist.stock_tracker.dtos.requests.CreateWatchlistRequest;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.Watchlist;
import com.watchlist.stock_tracker.repositories.WatchlistRepository;
import com.watchlist.stock_tracker.transformers.WatchlistTransformers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository repository;

    public WatchlistService(WatchlistRepository repository) {
        this.repository = repository;
    }

    public List<WatchlistDto> getWatchlistsByUserId(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(WatchlistTransformers::getWatchlistResponse)
                .toList();
    }

    public WatchlistDto createWatchlist(CreateWatchlistRequest request) {
        Watchlist watchlist = repository.save(Watchlist.builder()
                .name(request.getName())
                .userId(request.getUserId())
                .build());
        return WatchlistTransformers.getWatchlistResponse(watchlist);
    }

    public void deleteWatchlist(Long id, long userId) {
        Watchlist watchlist = getWatchlistById(id);
        if (watchlist != null) {
            if (watchlist.getUserId() == userId) {
                repository.deleteById(id);
            } else {
                throw new CustomException("User not authorized to delete this resource", 403);
            }
        } else {
            throw new CustomException("Invalid watchlist Id", 400);
        }
    }

    public Watchlist getWatchlistById(long watchlistId) {
        if (watchlistId == 0) return null;
        return repository.findById(watchlistId).orElse(null);
    }
}
