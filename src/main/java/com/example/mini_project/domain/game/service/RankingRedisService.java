package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.entity.Ranking;
import com.example.mini_project.domain.user.entity.User;

import java.util.List;

public interface RankingRedisService {
    void createRanking(User user, int level, int gameScore);

    List<Ranking> getTopRankings(int topN);
}
