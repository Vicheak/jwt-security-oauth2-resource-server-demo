package com.vicheak.coreapp.service;

import com.vicheak.coreapp.dto.StudentDto;
import com.vicheak.coreapp.dto.StudentTransactionDto;
import com.vicheak.coreapp.entity.Student;

import java.util.List;

public interface StudentService {

    List<StudentDto> loadAllStudents();

    StudentDto loadStudentByUuid(String uuid);

    Student getStudentByUuid(String uuid);

    void createNewStudent(StudentTransactionDto studentTransactionDto);

    void updateStudentByUuid(String uuid, StudentTransactionDto studentTransactionDto);

    void deleteStudentByUuid(String uuid);

}
