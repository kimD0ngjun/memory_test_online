package com.example.mini_project.domain.game.memory.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "ranking")
public class MemoryTestRanking {
    @Id
    private String email;
    private String username;
    private int level;
    private int gameScore;
    private int score;

    private static final int MULTIPLE = 1_000_000;

    public MemoryTestRanking(String email, String username, int level, int gameScore) {
        this.email = email;
        this.username = username;
        this.level = level;
        this.gameScore = gameScore;
        this.score = calculateScore(level, gameScore);
    }

    private int calculateScore(int level, int gameScore) {
        return level * MULTIPLE + gameScore;
    }

//    @Override
//    // ZSet의 첫 번째 동등성 비교 기준
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true; // 같은 객체면 참
//        }
//
//        if (obj == null || getClass() != obj.getClass()) {
//            return false; // obj 값이 null 혹은 클래스 타입 불일치 판별
//        }
//
//        MemoryTestRanking that = (MemoryTestRanking) obj; // 명시적 형변
//        return Objects.equals(email, that.email); // email 필드로 동일 여부 확인(ZSet의 첫 번째 동등성 비교 기준)
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(email);
//    }
}
