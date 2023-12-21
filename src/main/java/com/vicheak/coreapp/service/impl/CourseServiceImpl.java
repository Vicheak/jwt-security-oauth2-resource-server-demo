package com.vicheak.coreapp.service.impl;

import com.vicheak.coreapp.dto.CourseDto;
import com.vicheak.coreapp.dto.CourseTransactionDto;
import com.vicheak.coreapp.entity.Course;
import com.vicheak.coreapp.mapper.CourseMapper;
import com.vicheak.coreapp.repository.CourseRepository;
import com.vicheak.coreapp.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseDto> loadAllCourses() {
        return courseMapper.toCourseDto(courseRepository.findAll());
    }

    @Override
    public CourseDto loadCourseByUuid(String uuid) {
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Course with uuid, %s not found...!"
                                        .formatted(uuid))
                );

        return courseMapper.toCourseDto(course);
    }

    @Override
    public Course getCourseByUuid(String uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Course with uuid, %s not found...!"
                                        .formatted(uuid))
                );
    }

    @Override
    public void createNewCourse(CourseTransactionDto courseTransactionDto) {
        Course course = courseMapper.fromCourseTransactionDto(courseTransactionDto);
        course.setUuid(UUID.randomUUID().toString());

        courseRepository.save(course);
    }

    @Override
    public void updateCourseByUuid(String uuid, CourseTransactionDto courseTransactionDto) {
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Course with uuid, %s not found...!"
                                        .formatted(uuid))
                );

        courseMapper.fromCourseTransactionDto(course, courseTransactionDto);

        courseRepository.save(course);
    }

    @Override
    public void deleteCourseByUuid(String uuid) {
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Course with uuid, %s not found...!"
                                        .formatted(uuid))
                );

        courseRepository.delete(course);
    }
}
