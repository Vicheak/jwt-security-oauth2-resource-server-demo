package com.vicheak.coreapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentTransactionDto(@NotBlank
                                    String name,

                                    @NotBlank
                                    @Email
                                    String email) {
}
