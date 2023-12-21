package com.vicheak.coreapp.auth.web;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(@NotBlank(message = "Refresh Token should not be blank!")
                              String refreshToken) {
}
