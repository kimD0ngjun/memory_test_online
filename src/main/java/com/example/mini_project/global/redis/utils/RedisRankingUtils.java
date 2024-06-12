package com.example.mini_project.global.redis.utils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisRankingUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ZSetOperations<String, Object> zSetOps;

    public RedisRankingUtils(
            RedisTemplate<String, Object> redisTemplate,
            @Qualifier("redisRankingTemplate") ZSetOperations<String, Object> zSetOps) {
        this.redisTemplate = redisTemplate;
        this.zSetOps = zSetOps;
    }

    /**
     해당 메소드들은 그냥 임의로 만든 메소드임
     향후 랭킹을 위한 관련 엔티티가 생성된다면 거기에 맞춰서 랭킹 구현 방법을 고민하기
     * */

    public void addScore(String key, String member, double score) {
        zSetOps.add(key, member, score);
    }

    public Set<Object> getTopRankings(String key, int count) {
        return zSetOps.reverseRange(key, 0, count - 1);
    }

    public Double getScore(String key, String member) {
        return zSetOps.score(key, member);
    }
}
