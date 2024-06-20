package com.example.mini_project.domain.user.dto;

import lombok.Getter;

@Getter
public class UserLoginRequestDto {
    private String email;
    private String password;
}
