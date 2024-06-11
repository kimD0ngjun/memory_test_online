package com.example.mini_project.domain.mapper;

import com.example.mini_project.domain.dto.UserDto;
import com.example.mini_project.domain.entity.User;
import com.example.mini_project.domain.entity.UserRoleEnum;

// Employee 엔티티와 EmployeeDto dto를 매핑하는 Mapper 클래스
public class UserMapper {

    // entity -> dto
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    // dto -> entity
    // Role : User
    public static User mapToUser(UserDto userDto, String password) {
        return new User(
                userDto.getUsername(),
                userDto.getEmail(),
                password,
                UserRoleEnum.USER);
    }

    // Role : Admin
    public static User mapToAdmin(UserDto userDto, String password) {
        return new User(
                userDto.getUsername(),
                userDto.getEmail(),
                password,
                UserRoleEnum.ADMIN);
    }
}
