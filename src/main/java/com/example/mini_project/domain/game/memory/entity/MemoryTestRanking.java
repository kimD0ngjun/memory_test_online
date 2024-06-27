package com.example.mini_project.domain.game.memory.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisHash;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//@RedisHash(value = "ranking")
@Slf4j
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

    @Override
    // ZSet의 두 번째 동등성 비교 기준(얘 입장에서 새로 추가되는 객체니까?)
    public boolean equals(Object obj) {
        // 테스트용 오버라이딩
        // 동일한 객체를 가리키는 경우
        if (this == obj) {
            return true;
        }

        // obj 값이 null 혹은 클래스 타입 불일치 판별
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MemoryTestRanking that = (MemoryTestRanking) obj; // 명시적 형변
        log.info("that 의 이메일이랑 점수: " + that.email + ", " + that.score);
        log.info("인스턴스의 이메일: " + email + ", " + score);
        return that.email.equals(email);
    }

    // 첫 번째 객체 동등 여부
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
