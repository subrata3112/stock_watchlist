package com.watchlist.stock_tracker.services;

import com.watchlist.stock_tracker.dtos.requests.LoginRequest;
import com.watchlist.stock_tracker.dtos.requests.TokenRefreshRequest;
import com.watchlist.stock_tracker.dtos.responses.LoginResponse;
import com.watchlist.stock_tracker.dtos.responses.SignupResponse;
import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.User;
import com.watchlist.stock_tracker.repositories.UserRepository;
import com.watchlist.stock_tracker.services.security.RefreshTokenService;
import com.watchlist.stock_tracker.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;

    public LoginResponse login(LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword())
        );

        User user = (User) auth.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername()).getRefreshToken();

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .refreshToken(refreshToken)
                .build();
    }

    public @Nullable SignupResponse signup(LoginRequest request) {

        var user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user != null) throw new CustomException("Username already present", 400);
        user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) throw new CustomException("Email already registered", 400);

        user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        var created = userRepository.save(user);
        return SignupResponse.builder()
                .userId(created.getId())
                .username(created.getUsername())
                .build();
    }


    public @Nullable LoginResponse refreshToken(TokenRefreshRequest request) {
        var refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        return LoginResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .token(authUtil.generateAccessToken(refreshToken.getUser()))
                .userId(refreshToken.getUser().getId())
                .build();
    }
}
