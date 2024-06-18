package com.example.mini_project.global.auth.repository;

import com.example.mini_project.global.auth.entity.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRedisRepository {
    private final RedisTemplate<String, Token> redisTemplate;

    @Value("${custom.redis.token.key}")
    private String tokenKey;

    @Value("custom.redis.token.access-key")
    private String accessTokenKey;

    // Token 저장
    public void saveToken(Token token) {
        redisTemplate.opsForHash().put(tokenKey, token.getEmail(), token);
        redisTemplate.opsForHash().put(accessTokenKey, token.getAccessToken(), token);
    }

    // 이메일로 Token 조회
    public Token findTokenByEmail(String email) {
        return (Token) redisTemplate.opsForHash().get(tokenKey, email);
    }

    // 엑세스토큰으로 Token 조회
    public Token findTokenByAccessToken(String accessToken) {
        return (Token) redisTemplate.opsForHash().get(accessTokenKey, accessToken);
    }

    // 이메일로 Token 삭제
    public void deleteTokenByEmail(String email) {
        Token token = findTokenByEmail(email);
        if (token != null) {
            redisTemplate.opsForHash().delete(tokenKey, email);
            redisTemplate.opsForHash().delete(accessTokenKey, token.getAccessToken());
        }
    }
}
