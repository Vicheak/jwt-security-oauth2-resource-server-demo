package com.vicheak.coreapp.controller;

import com.vicheak.coreapp.dto.StudentDto;
import com.vicheak.coreapp.dto.StudentTransactionDto;
import com.vicheak.coreapp.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> loadAllStudents() {
        return studentService.loadAllStudents();
    }

    @GetMapping("/{uuid}")
    public StudentDto loadStudentByUuid(@PathVariable String uuid) {
        return studentService.loadStudentByUuid(uuid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewStudent(@RequestBody @Valid StudentTransactionDto studentTransactionDto) {
        studentService.createNewStudent(studentTransactionDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public void updateStudentByUuid(@PathVariable String uuid,
                                    @RequestBody StudentTransactionDto studentTransactionDto) {
        studentService.updateStudentByUuid(uuid, studentTransactionDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public void deleteStudentByUuid(@PathVariable String uuid){
        studentService.deleteStudentByUuid(uuid);
    }

}
