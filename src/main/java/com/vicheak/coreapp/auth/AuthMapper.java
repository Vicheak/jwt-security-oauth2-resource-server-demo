package com.vicheak.coreapp.auth;

import com.vicheak.coreapp.auth.web.RegisterDto;
import com.vicheak.coreapp.dto.NewUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    NewUserDto mapFromRegisterDtoToNewUserDto(RegisterDto registerDto);

}
