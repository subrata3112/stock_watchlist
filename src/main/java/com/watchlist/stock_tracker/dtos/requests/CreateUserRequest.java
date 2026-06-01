package com.watchlist.stock_tracker.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

    private String name;

    @Email
    @NotBlank
    private String email;

    private String password;
}
