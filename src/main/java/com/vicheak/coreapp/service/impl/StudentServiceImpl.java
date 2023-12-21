package com.vicheak.coreapp.service.impl;

import com.vicheak.coreapp.dto.StudentDto;
import com.vicheak.coreapp.dto.StudentTransactionDto;
import com.vicheak.coreapp.entity.Student;
import com.vicheak.coreapp.mapper.StudentMapper;
import com.vicheak.coreapp.repository.StudentRepository;
import com.vicheak.coreapp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentDto> loadAllStudents() {
        return studentMapper.toStudentDto(studentRepository.findAll());
    }

    @Override
    public StudentDto loadStudentByUuid(String uuid) {
        Student student = studentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Student with uuid, %s not found...!"
                                        .formatted(uuid))
                );

        return studentMapper.toStudentDto(student);
    }

    @Override
    public Student getStudentByUuid(String uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Student with uuid, %s not found...!"
                                        .formatted(uuid))
                );
    }

    @Override
    public void createNewStudent(StudentTransactionDto studentTransactionDto) {
        Student student = studentMapper.fromStudentTransactionDto(studentTransactionDto);
        student.setUuid(UUID.randomUUID().toString());

        studentRepository.save(student);
    }

    @Override
    public void updateStudentByUuid(String uuid, StudentTransactionDto studentTransactionDto) {
        Student student = studentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Student with uuid, %s not found...!"
                                        .formatted(uuid))
                );

        studentMapper.fromStudentTransactionDto(student, studentTransactionDto);

        studentRepository.save(student);
    }

    @Override
    public void deleteStudentByUuid(String uuid) {
        Student student = studentRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Student with uuid, %s not found...!"
                                        .formatted(uuid))
                );

        studentRepository.delete(student);
    }

}
