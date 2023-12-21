package com.vicheak.coreapp.mapper;

import com.vicheak.coreapp.dto.RatingCourseDto;
import com.vicheak.coreapp.dto.RatingTransactionDto;
import com.vicheak.coreapp.entity.CourseRating;
import com.vicheak.coreapp.service.CourseService;
import com.vicheak.coreapp.service.StudentService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentService.class, CourseService.class})
public interface RatingMapper {

    @Mapping(target = "student", source = "studentUuid")
    @Mapping(target = "course", source = "courseUuid")
    CourseRating fromRatingTransactionDto(RatingTransactionDto ratingTransactionDto);

    @Mapping(target = "courseName", source = "course.name")
    RatingCourseDto toRatingCourseDto(CourseRating courseRating);

    List<RatingCourseDto> toRatingCourseDto(List<CourseRating> courseRatings);

}
