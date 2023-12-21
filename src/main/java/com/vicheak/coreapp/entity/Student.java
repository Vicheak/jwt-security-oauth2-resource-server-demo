package com.vicheak.coreapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "student_name", length = 30, nullable = false)
    private String name;

    @Column(name = "student_email", length = 50, nullable = false)
    private String email;

//    @ManyToMany
//    @JoinTable(name = "students_courses",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id"))
//    private Set<Course> courses;

    @OneToMany(mappedBy = "student")
    private Set<CourseRating> ratings;

}
