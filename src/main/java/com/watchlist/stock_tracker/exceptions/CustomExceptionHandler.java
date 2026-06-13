package com.watchlist.stock_tracker.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.warn("Custom exception caught: {} - Code: {}", e.getMessage(), e.getCode());
        return ResponseEntity.status(e.getCode())
                .body(new ErrorResponse(HttpStatus.valueOf(e.getCode()), e.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleCustomException(Exception e) {
        log.error("Unexpected exception caught", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
