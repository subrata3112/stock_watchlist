package com.watchlist.stock_tracker.controllers;

import com.watchlist.stock_tracker.dtos.requests.LoginRequest;
import com.watchlist.stock_tracker.dtos.responses.LoginResponse;
import com.watchlist.stock_tracker.dtos.responses.SignupResponse;
import com.watchlist.stock_tracker.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

}
