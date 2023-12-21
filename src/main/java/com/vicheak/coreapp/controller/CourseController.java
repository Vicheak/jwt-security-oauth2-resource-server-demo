package com.vicheak.coreapp.controller;

import com.vicheak.coreapp.dto.CourseDto;
import com.vicheak.coreapp.dto.CourseTransactionDto;
import com.vicheak.coreapp.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDto> loadAllCourses() {
        return courseService.loadAllCourses();
    }

    @GetMapping("/{uuid}")
    public CourseDto loadCourseByUuid(@PathVariable String uuid) {
        return courseService.loadCourseByUuid(uuid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewCourse(@RequestBody @Valid CourseTransactionDto courseTransactionDto) {
        courseService.createNewCourse(courseTransactionDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public void updateCourseByUuid(@PathVariable String uuid,
                                   @RequestBody CourseTransactionDto courseTransactionDto) {
        courseService.updateCourseByUuid(uuid, courseTransactionDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteCourseByUuid(@PathVariable String uuid) {
        courseService.deleteCourseByUuid(uuid);
    }

}
