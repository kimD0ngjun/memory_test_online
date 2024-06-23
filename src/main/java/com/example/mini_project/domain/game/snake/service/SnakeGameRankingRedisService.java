package com.example.mini_project.domain.game.snake.service;


import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.game.common.service.RankingRedisService;
import com.example.mini_project.domain.game.snake.entity.SnakeGameRanking;
import com.example.mini_project.domain.game.snake.repository.SnakeGameRankingRedisRepository;
import com.example.mini_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SnakeGameRankingRedisService implements RankingRedisService {

    private final SnakeGameRankingRedisRepository snakeGameRankingRedisRepository;

    @Override
    public void createRanking(User user, int level, int gameScore) {
        SnakeGameRanking snakeGameRanking = new SnakeGameRanking(user.getEmail(), user.getUsername(), level, gameScore);

        snakeGameRankingRedisRepository.save(snakeGameRanking);
    }

    @Override
    public List<RankingResponseDto> getTopRankings(int topN) {
        List<SnakeGameRanking> snakeGameRankings = snakeGameRankingRedisRepository.getRankingRange(0, topN - 1);

        return IntStream.range(0, snakeGameRankings.size()).
                mapToObj(i -> new RankingResponseDto(snakeGameRankings.get(i), i + 1)).toList();
    }
}
