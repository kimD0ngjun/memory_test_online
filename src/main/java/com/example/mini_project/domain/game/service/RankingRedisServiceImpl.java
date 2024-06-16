package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.entity.Ranking;
import com.example.mini_project.domain.game.repository.RankingRedisRepository;
import com.example.mini_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingRedisServiceImpl implements RankingRedisService {

    private final RankingRedisRepository rankingRedisRepository;

    // 랭킹 생성
    @Override
    public void createRanking(User user, int level, int gameScore) {
        Double score = level * Math.pow(10, 20) * level + gameScore;
        Ranking ranking = new Ranking(user.getEmail(), score);

        rankingRedisRepository.save(ranking);
    }

    // 랭킹 반환
    @Override
    public List<Ranking> getTopRankings(int topN) {
        return rankingRedisRepository.getRankingRange(0, topN - 1);
    }
}
