package com.vicheak.coreapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

public record UpdateRatingDto(@NotBlank
                              String courseUuid,

                              @NotNull
                              @Range(min = 0, max = 10)
                              @Positive
                              Double rating) {
}
