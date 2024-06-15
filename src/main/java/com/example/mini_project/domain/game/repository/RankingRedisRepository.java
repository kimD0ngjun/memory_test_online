package com.example.mini_project.domain.game.repository;

import com.example.mini_project.domain.game.entity.Ranking;
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
public class RankingRedisRepository {

    private final RedisTemplate<String, Ranking> redisTemplate;
    private ZSetOperations<String, Ranking> zSetOperations;

    @Value("${custom.redis.ranking.key}")
    private String rankingKey;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    public void save(Ranking ranking) {
        zSetOperations.add(rankingKey, ranking, ranking.getScore());
    }

    public List<Ranking> getRankingRange(int start, int end) {
        Set<Ranking> rankings = zSetOperations.reverseRange(rankingKey, start, end);

        if (rankings == null || rankings.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(rankings);
    }

    // delta 는 특정 값에 대해 증가 또는 감소할 양을 나타내는 변수
    public void increaseScore(String email, double delta) {
        Ranking ranking = findById(email);
        if (ranking != null) {
            zSetOperations.incrementScore(rankingKey, ranking, delta);
        }
    }

    public void decreaseScore(String email, double delta) {
        Ranking ranking = findById(email);
        if (ranking != null && delta > 0) {
            zSetOperations.incrementScore(rankingKey, ranking, -delta);
        }
    }

    public Ranking findById(String email) {
        // redis 에서는 ZSet 에서 직접 객체를 검색하는 기능이 없으므로, 별도로 저장된 객체를 조회
        return redisTemplate.opsForValue().get(email);
    }

    public void remove(String email) {
        Ranking ranking = findById(email);
        if (ranking != null) {
            zSetOperations.remove(rankingKey, ranking);
        }
    }
}
