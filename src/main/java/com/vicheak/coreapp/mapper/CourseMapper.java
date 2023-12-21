package com.vicheak.coreapp.mapper;

import com.vicheak.coreapp.dto.CourseDto;
import com.vicheak.coreapp.dto.CourseTransactionDto;
import com.vicheak.coreapp.entity.Course;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto toCourseDto(Course course);

    List<CourseDto> toCourseDto(List<Course> courses);

    Course fromCourseTransactionDto(CourseTransactionDto courseTransactionDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromCourseTransactionDto(@MappingTarget Course course, CourseTransactionDto courseTransactionDto);

}
