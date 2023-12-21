package com.vicheak.coreapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "course_name", length = 30, nullable = false)
    private String name;

//    @ManyToMany(mappedBy = "courses")
//    private Set<Student> students;

    @OneToMany(mappedBy = "course")
    private Set<CourseRating> ratings;

}
