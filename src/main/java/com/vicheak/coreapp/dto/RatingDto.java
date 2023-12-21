package com.vicheak.coreapp.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RatingDto(StudentDto studentInfo,

                        List<RatingCourseDto> ratingCourseList) {
}
