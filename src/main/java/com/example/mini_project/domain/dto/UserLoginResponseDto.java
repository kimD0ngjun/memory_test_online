package com.example.mini_project.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserLoginResponseDto {
    private String message;

    public UserLoginResponseDto(String message) {
        this.message = message;
    }
}
