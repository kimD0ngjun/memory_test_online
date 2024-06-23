package com.example.mini_project.domain.game.common.service;

import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.user.entity.User;

import java.util.List;

public interface RankingRedisService {
    void createRanking(User user, int level, int gameScore);

    List<RankingResponseDto> getTopRankings(int topN);
}
