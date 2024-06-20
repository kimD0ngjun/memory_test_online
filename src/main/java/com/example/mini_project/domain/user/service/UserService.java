package com.example.mini_project.domain.user.service;

import com.example.mini_project.domain.user.dto.UserDto;
import com.example.mini_project.domain.user.entity.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void updateUsername(User user, UserDto updatedUser);

    void deleteUser(Long userId);
}
