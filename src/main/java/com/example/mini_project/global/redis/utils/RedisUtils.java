package com.example.mini_project.global.redis.utils;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUtils {

    // key : username(email), value : refreshToken
    private final RedisTemplate<String, String> refreshTokenTemplate;

    public void saveRefreshToken(String key, String value){
        refreshTokenTemplate.opsForValue().set(key, value);
    }

    public String getRefreshToken(String key){
        return refreshTokenTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String key){
        refreshTokenTemplate.delete(key);
    }
}