package com.example.mini_project.global.auth.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "token")
public class Token {
    @Id
    private String email;

    @Indexed
    private String accessToken;

    private String refreshToken;
}
