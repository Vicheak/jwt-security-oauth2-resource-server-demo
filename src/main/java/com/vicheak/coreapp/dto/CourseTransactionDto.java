package com.vicheak.coreapp.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseTransactionDto(@NotBlank
                                   String name) {
}
