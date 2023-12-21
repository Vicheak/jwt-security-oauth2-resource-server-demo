package com.vicheak.coreapp.mapper;

import com.vicheak.coreapp.dto.StudentDto;
import com.vicheak.coreapp.dto.StudentTransactionDto;
import com.vicheak.coreapp.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student fromStudentTransactionDto(StudentTransactionDto studentTransactionDto);

    StudentDto toStudentDto(Student student);

    List<StudentDto> toStudentDto(List<Student> students);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromStudentTransactionDto(@MappingTarget Student student, StudentTransactionDto studentTransactionDto);

}
