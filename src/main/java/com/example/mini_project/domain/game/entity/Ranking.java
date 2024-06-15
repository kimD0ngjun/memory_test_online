package com.example.mini_project.domain.game.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "ranking")
public class Ranking {
    @Id
    private String email;

    private Double score;
}
