package com.vicheak.coreapp.service;

import com.vicheak.coreapp.dto.CourseDto;
import com.vicheak.coreapp.dto.CourseTransactionDto;
import com.vicheak.coreapp.entity.Course;

import java.util.List;

public interface CourseService {

    List<CourseDto> loadAllCourses();

    CourseDto loadCourseByUuid(String uuid);

    Course getCourseByUuid(String uuid);

    void createNewCourse(CourseTransactionDto courseTransactionDto);

    void updateCourseByUuid(String uuid, CourseTransactionDto courseTransactionDto);

    void deleteCourseByUuid(String uuid);

}
