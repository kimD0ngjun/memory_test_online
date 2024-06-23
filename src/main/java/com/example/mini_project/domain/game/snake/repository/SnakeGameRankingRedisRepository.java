package com.example.mini_project.domain.game.snake.repository;

import com.example.mini_project.domain.game.memory.entity.MemoryTestRanking;
import com.example.mini_project.domain.game.snake.entity.SnakeGameRanking;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SnakeGameRankingRedisRepository {

    private final RedisTemplate<String, SnakeGameRanking> redisTemplate;
    private ZSetOperations<String, SnakeGameRanking> zSetOperations;

    @Value("${custom.redis.snake.key}")
    private String rankingKey;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    public void save(SnakeGameRanking snakeGameRanking) {
        zSetOperations.add(rankingKey, snakeGameRanking, snakeGameRanking.getScore());
    }

    public List<SnakeGameRanking> getRankingRange(int start, int end) {
        Set<SnakeGameRanking> snakeGameRankings = zSetOperations.reverseRange(rankingKey, start, end);

        if (snakeGameRankings == null || snakeGameRankings.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(snakeGameRankings);
    }

    // delta 는 특정 값에 대해 증가 또는 감소할 양을 나타내는 변수
    public void increaseScore(String email, double delta) {
        SnakeGameRanking snakeGameRanking = findById(email);
        if (snakeGameRanking != null) {
            zSetOperations.incrementScore(rankingKey, snakeGameRanking, delta);
        }
    }

    public void decreaseScore(String email, double delta) {
        SnakeGameRanking snakeGameRanking = findById(email);
        if (snakeGameRanking != null && delta > 0) {
            zSetOperations.incrementScore(rankingKey, snakeGameRanking, -delta);
        }
    }

    public SnakeGameRanking findById(String email) {
        // redis 에서는 ZSet 에서 직접 객체를 검색하는 기능이 없으므로, 별도로 저장된 객체를 조회
        return redisTemplate.opsForValue().get(email);
    }

    public void remove(String email) {
        SnakeGameRanking snakeGameRanking = findById(email);
        if (snakeGameRanking != null) {
            zSetOperations.remove(rankingKey, snakeGameRanking);
        }
    }
}
