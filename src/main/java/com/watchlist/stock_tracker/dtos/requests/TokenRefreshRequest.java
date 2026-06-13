package com.watchlist.stock_tracker.dtos.requests;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
}
