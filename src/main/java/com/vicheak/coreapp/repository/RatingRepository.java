package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.entity.Course;
import com.vicheak.coreapp.entity.CourseRating;
import com.vicheak.coreapp.entity.CourseRatingKey;
import com.vicheak.coreapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<CourseRating, CourseRatingKey> {

    boolean existsByStudentAndCourse(Student student, Course course);

    CourseRating findByStudentAndCourse(Student student, Course course);

     List<CourseRating> findByStudent(Student student);

}
