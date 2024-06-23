package com.example.mini_project.domain.game.snake.service;

import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.user.entity.User;

import java.util.List;

public interface SnakeGameRankingRedisService {
    void createRanking(User user, int level, int gameScore);
    List<RankingResponseDto> getTopRankings(int topN);
}
