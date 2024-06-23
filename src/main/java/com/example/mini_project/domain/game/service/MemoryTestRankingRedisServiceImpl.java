package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.dto.RankingResponseDto;
import com.example.mini_project.domain.game.entity.MemoryTestRanking;
import com.example.mini_project.domain.game.repository.MemoryTestRankingRedisRepository;
import com.example.mini_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MemoryTestRankingRedisServiceImpl implements MemoryTestRankingRedisService {

    private final MemoryTestRankingRedisRepository memoryTestRankingRedisRepository;

    // 랭킹 생성
    @Override
    public void createRanking(User user, int level, int gameScore) {
        MemoryTestRanking memoryTestRanking = new MemoryTestRanking(user.getEmail(), user.getUsername(), level, gameScore);

        memoryTestRankingRedisRepository.save(memoryTestRanking);
    }

    // 랭킹 반환
    @Override
    public List<RankingResponseDto> getTopRankings(int topN) {
        List<MemoryTestRanking> memoryTestRankings = memoryTestRankingRedisRepository.getRankingRange(0, topN - 1);

        return IntStream.range(0, memoryTestRankings.size()).
                mapToObj(i -> new RankingResponseDto(memoryTestRankings.get(i), i + 1)).toList();
    }
}
