package com.example.mini_project.domain.game.memory.repository;

import com.example.mini_project.domain.game.memory.entity.MemoryTestRanking;
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
public class MemoryTestRankingRedisRepository {

    private final RedisTemplate<String, MemoryTestRanking> redisTemplate;
    private ZSetOperations<String, MemoryTestRanking> zSetOperations;

    @Value("${custom.redis.memory.key}")
    private String rankingKey;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    // key 는 rankingKey
    // 그래서 같은 이메일이어도 여러 개의 점수가 순위권에 오를 수 있음
    /**
     * 해야 될 거
     * 1. (1) 레벨, (2) 점수 기준으로 고유한 값 뽑아내기
     * -> 얘는 '레벨 * 10^20 + 점수'를 저장할 score로 처리하는 거 좋은 듯?
     * 2. 완벽하게 같은 동점자 처리하기는 이름순?
     * */
    public void save(MemoryTestRanking memoryTestRanking) {
        zSetOperations.add(rankingKey, memoryTestRanking, memoryTestRanking.getScore());
    }

    public List<MemoryTestRanking> getRankingRange(int start, int end) {
        Set<MemoryTestRanking> memoryTestRankings = zSetOperations.reverseRange(rankingKey, start, end);

        if (memoryTestRankings == null || memoryTestRankings.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(memoryTestRankings);
    }

    // delta 는 특정 값에 대해 증가 또는 감소할 양을 나타내는 변수
    public void increaseScore(String email, double delta) {
        MemoryTestRanking memoryTestRanking = findById(email);
        if (memoryTestRanking != null) {
            zSetOperations.incrementScore(rankingKey, memoryTestRanking, delta);
        }
    }

    public void decreaseScore(String email, double delta) {
        MemoryTestRanking memoryTestRanking = findById(email);
        if (memoryTestRanking != null && delta > 0) {
            zSetOperations.incrementScore(rankingKey, memoryTestRanking, -delta);
        }
    }

    public MemoryTestRanking findById(String email) {
        // redis 에서는 ZSet 에서 직접 객체를 검색하는 기능이 없으므로, 별도로 저장된 객체를 조회
        return redisTemplate.opsForValue().get(email);
    }

    public void remove(String email) {
        MemoryTestRanking memoryTestRanking = findById(email);
        if (memoryTestRanking != null) {
            zSetOperations.remove(rankingKey, memoryTestRanking);
        }
    }
}
