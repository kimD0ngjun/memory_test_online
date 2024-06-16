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
    private String username;
    private int level;
    private int gameScore;
    private Double score;

    public Ranking(String email, String username, int level, int gameScore) {
        this.email = email;
        this.username = username;
        this.level = level;
        this.gameScore = gameScore;
        this.score = calculateScore(level, gameScore);
    }

    private Double calculateScore(int level, int gameScore) {
        return level * Math.pow(10, 20) * level + gameScore;
    }
}
