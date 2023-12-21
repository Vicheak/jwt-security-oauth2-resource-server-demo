package com.vicheak.coreapp.dto;

import lombok.Builder;

@Builder
public record RatingCourseDto(String courseName,

                              Double rating) {
}
