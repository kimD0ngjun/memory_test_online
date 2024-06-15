package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.entity.Ranking;
import com.example.mini_project.domain.game.repository.RankingRedisRepository;
import com.example.mini_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingRedisService {

    private final RankingRedisRepository rankingRedisRepository;

    // 랭킹 생성
    public void createRanking(User user, int level, int gameScore) {
        Double score = level * Math.pow(10, 20) * level + gameScore;
        Ranking ranking = new Ranking(user.getEmail(), score);

        rankingRedisRepository.save(ranking);
    }

}
