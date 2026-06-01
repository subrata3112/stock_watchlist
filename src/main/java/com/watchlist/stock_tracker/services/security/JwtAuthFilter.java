package com.watchlist.stock_tracker.services.security;

import com.watchlist.stock_tracker.exceptions.CustomException;
import com.watchlist.stock_tracker.models.User;
import com.watchlist.stock_tracker.repositories.UserRepository;
import com.watchlist.stock_tracker.utils.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request: {}", request.getRequestURI());
        try {
            String authToken = request.getHeader("Authorization");
            if (authToken == null || !authToken.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authToken.split("Bearer ")[1];
            String username = authUtil.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsername(username).orElse(null);
                if (user == null) {
                    throw new CustomException("Invalid username", 400);
                }
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
