package com.vicheak.coreapp.service.impl;

import com.vicheak.coreapp.dto.*;
import com.vicheak.coreapp.entity.Course;
import com.vicheak.coreapp.entity.CourseRating;
import com.vicheak.coreapp.entity.CourseRatingKey;
import com.vicheak.coreapp.entity.Student;
import com.vicheak.coreapp.mapper.RatingMapper;
import com.vicheak.coreapp.mapper.StudentMapper;
import com.vicheak.coreapp.repository.RatingRepository;
import com.vicheak.coreapp.repository.StudentRepository;
import com.vicheak.coreapp.service.CourseService;
import com.vicheak.coreapp.service.RatingService;
import com.vicheak.coreapp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentService studentService;
    private final CourseService courseService;

    @Override
    public RatingDto loadRatingByStudentUuid(String studentUuid) {
        //load the student by uuid
        Student student = studentRepository.findByUuid(studentUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Student with uuid, %s not found...!"
                                        .formatted(studentUuid))
                );

        //load course rating by student
        List<CourseRating> courseRating = ratingRepository.findByStudent(student);

        //mapping methods
        StudentDto studentDto = studentMapper.toStudentDto(student);
        List<RatingCourseDto> ratingCourseDtoList = ratingMapper.toRatingCourseDto(courseRating);

        return RatingDto.builder()
                .studentInfo(studentDto)
                .ratingCourseList(ratingCourseDtoList)
                .build();
    }

    @Override
    public void createNewRating(RatingTransactionDto ratingTransactionDto) {
        //map from dto to entity
        CourseRating courseRating =
                ratingMapper.fromRatingTransactionDto(ratingTransactionDto);

        //set the composite key
        courseRating.setId(CourseRatingKey.builder()
                .studentId(courseRating.getStudent().getId())
                .courseId(courseRating.getCourse().getId())
                .build());

        ratingRepository.save(courseRating);
    }

    @Override
    public void updateRating(String studentUuid, UpdateRatingDto updateRatingDto) {
        //load student by uuid
        Student student = studentService.getStudentByUuid(studentUuid);

        //load course by uuid
        Course course = courseService.getCourseByUuid(updateRatingDto.courseUuid());

        //check the courses of student
        if (!ratingRepository.existsByStudentAndCourse(student, course))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The student does not rate this course...!");

        CourseRating courseRating =
                ratingRepository.findByStudentAndCourse(student, course);
        courseRating.setRating(updateRatingDto.rating());

        ratingRepository.save(courseRating);
    }

    @Override
    public void deleteRating(String studentUuid) {
        //load the student by uuid
        Student student = studentRepository.findByUuid(studentUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Student with uuid, %s not found...!"
                                        .formatted(studentUuid))
                );

        //load course rating by student
        List<CourseRating> courseRating = ratingRepository.findByStudent(student);

        ratingRepository.deleteAll(courseRating);
    }

}
