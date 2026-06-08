package com.watchlist.stock_tracker.repositories;

import com.watchlist.stock_tracker.models.WatchlistComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistCompositionRepository extends JpaRepository<WatchlistComposition, Long> {

    List<WatchlistComposition> findAllByWatchlistId(long watchlistId);

    Optional<WatchlistComposition> findByWatchlistIdAndTickerId(long watchlistId, long stockId);
}
