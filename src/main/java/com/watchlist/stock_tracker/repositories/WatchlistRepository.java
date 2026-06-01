package com.watchlist.stock_tracker.repositories;

import com.watchlist.stock_tracker.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findAllByUserId(Long userId);
}
