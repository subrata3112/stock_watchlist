package com.watchlist.stock_tracker.services.security;

import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.RefreshToken;
import com.watchlist.stock_tracker.models.User;
import com.watchlist.stock_tracker.repositories.RefreshTokenRepository;
import com.watchlist.stock_tracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final static long expiryOffsetMillis = 60*60*5*1000;

    public RefreshToken createRefreshToken(String userName) {
        User user = getUserByUsername(userName);
        RefreshToken existing = user.getRefreshToken();
        if (existing != null) {
            existing.setExpiry(System.currentTimeMillis()+expiryOffsetMillis);
            refreshTokenRepository.save(existing);
            return existing;
        }

        RefreshToken newToken = RefreshToken.builder()
                .user(getUserByUsername(userName))
                .refreshToken(UUID.randomUUID().toString())
                .expiry(System.currentTimeMillis()+expiryOffsetMillis)
                .build();
        return refreshTokenRepository.save(newToken);
    }

    public RefreshToken verifyRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByRefreshToken(token).orElse(null);
        if (refreshToken == null) {
            throw new CustomException("Invalid Refresh Token!", 401);
        }
        if (System.currentTimeMillis() >= refreshToken.getExpiry()) {
            refreshTokenRepository.delete(refreshToken);
            throw new CustomException("Expired Refresh Token!", 401);
        };
        return refreshToken;
    }

    private User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow();
    }
}
