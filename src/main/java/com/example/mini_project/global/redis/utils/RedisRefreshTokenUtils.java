package com.example.mini_project.global.redis.utils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisRefreshTokenUtils {

    // key : username(email), value : refreshToken
    private final RedisTemplate<String, String> refreshTokenTemplate;

    public RedisRefreshTokenUtils(
            @Qualifier("redisRefreshTokenTemplate") RedisTemplate<String, String> refreshTokenTemplate) {
        this.refreshTokenTemplate = refreshTokenTemplate;
    }

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