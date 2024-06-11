package com.example.mini_project.global.auth.service;

import com.example.mini_project.global.dto.ApiMessageDto;

public interface AuthService {

    ApiMessageDto logout(String userEmail);

}
