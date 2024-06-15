package com.example.mini_project.global.auth.service;

import com.example.mini_project.global.dto.ApiMessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTemplate<String, String> redisRefreshToken;

    @Override
    public ApiMessageDto logout(String userEmail) {

        // 로그아웃 처리를 위한 리프레쉬 토큰 redis에서 삭제
        redisRefreshToken.delete(userEmail);

        if (redisRefreshToken.opsForValue().get(userEmail) != null) {
            throw new IllegalArgumentException("비정상적인 토큰 처리! 재확인 요망");
        }

        return new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 로그아웃됐습니다.");
    }
}
