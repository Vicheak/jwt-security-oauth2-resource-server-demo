package com.vicheak.coreapp.service.impl;

import com.vicheak.coreapp.dto.NewUserDto;
import com.vicheak.coreapp.entity.Role;
import com.vicheak.coreapp.entity.User;
import com.vicheak.coreapp.mapper.UserMapper;
import com.vicheak.coreapp.repository.RoleRepository;
import com.vicheak.coreapp.repository.UserRepository;
import com.vicheak.coreapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createNewUser(NewUserDto newUserDto) {
        //check if username already exists
        if (userRepository.existsByUsernameIgnoreCase(newUserDto.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username already existed in the system!");

        //check if email already exists
        if (userRepository.existsByEmailIgnoreCase(newUserDto.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already existed in the system!");

        //check if role id does not exist
        boolean isNotFound = newUserDto.roleIds().stream()
                .anyMatch(roleId -> !roleRepository.existsById(roleId));

        if (isNotFound)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Role ID has not been found in the system!");

        //map from dto to entity
        User user = userMapper.fromNewUserDto(newUserDto);

        user.setUuid(UUID.randomUUID().toString());
        //encrypt raw password via bcrypt algorithm
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(false);
        user.setIsDeleted(false);

        //set user role
        Set<Role> roles = newUserDto.roleIds().stream()
                .map(roleId -> Role.builder()
                        .id(roleId)
                        .build())
                .collect(Collectors.toSet());

        user.setRoles(roles);

        //save user into the database
        userRepository.save(user);
    }
}
