package com.watchlist.stock_tracker.dtos.requests;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String email;

    private String password;
}
