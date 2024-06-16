package com.example.mini_project.domain.game.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "ranking")
public class Ranking {
    @Id
    private String email;
    private String username;
    private int level;
    private int gameScore;
    private int score;

    private static final int MULTIPLE = 1_000_000;

    public Ranking(String email, String username, int level, int gameScore) {
        this.email = email;
        this.username = username;
        this.level = level;
        this.gameScore = gameScore;
        this.score = calculateScore(level, gameScore);
    }

    private int calculateScore(int level, int gameScore) {
        return level * MULTIPLE + gameScore;
    }
}
