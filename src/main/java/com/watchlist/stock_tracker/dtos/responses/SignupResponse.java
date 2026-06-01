package com.watchlist.stock_tracker.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponse {

    private long userId;
    private String username;
}
