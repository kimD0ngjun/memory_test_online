package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.dto.RankingResponseDto;
import com.example.mini_project.domain.game.entity.Ranking;
import com.example.mini_project.domain.game.repository.RankingRedisRepository;
import com.example.mini_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RankingRedisServiceImpl implements RankingRedisService {

    private final RankingRedisRepository rankingRedisRepository;

    // 랭킹 생성
    @Override
    public void createRanking(User user, int level, int gameScore) {
        Ranking ranking = new Ranking(user.getEmail(), user.getUsername(), level, gameScore);

        rankingRedisRepository.save(ranking);
    }

    // 랭킹 반환
    @Override
    public List<RankingResponseDto> getTopRankings(int topN) {
        List<Ranking> rankings = rankingRedisRepository.getRankingRange(0, topN - 1);

        return IntStream.range(0, rankings.size()).
                mapToObj(i -> new RankingResponseDto(rankings.get(i), i + 1)).toList();
    }
}
