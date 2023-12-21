package com.vicheak.coreapp.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record RatingTransactionDto(@NotBlank
                                   String studentUuid,

                                   @NotBlank
                                   String courseUuid,

                                   @NotNull
                                   @Range(min = 0, max = 10)
                                   @Positive
                                   Double rating) {
}
