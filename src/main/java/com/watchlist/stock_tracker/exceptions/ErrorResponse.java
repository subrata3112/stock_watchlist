package com.watchlist.stock_tracker.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String message;
}
