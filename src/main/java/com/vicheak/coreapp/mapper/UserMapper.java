package com.vicheak.coreapp.mapper;

import com.vicheak.coreapp.dto.NewUserDto;
import com.vicheak.coreapp.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromNewUserDto(NewUserDto newUserDto);

}
