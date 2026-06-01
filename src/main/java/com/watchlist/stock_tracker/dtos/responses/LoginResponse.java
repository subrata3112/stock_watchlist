package com.watchlist.stock_tracker.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;

    private long userId;
}
