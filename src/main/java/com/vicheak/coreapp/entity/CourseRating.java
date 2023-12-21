package com.vicheak.coreapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CourseRating {

    @EmbeddedId
    private CourseRatingKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private Double rating;

}
