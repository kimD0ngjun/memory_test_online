package com.example.mini_project.domain.game.common.controller;

import com.example.mini_project.domain.game.common.dto.RankingRequestDto;
import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.game.memory.service.MemoryTestRankingRedisService;
import com.example.mini_project.domain.game.snake.service.SnakeGameRankingRedisService;
import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.domain.user.entity.UserDetailsImpl;
import com.example.mini_project.global.dto.ApiMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mini")
public class RankingRedisController {

    private final MemoryTestRankingRedisService memoryTestRankingRedisService;
    private final SnakeGameRankingRedisService snakeGameRankingRedisService;

    private static final int TOP_RANKING = 10;

    @PostMapping("/memory_test/ranking")
    public ResponseEntity<ApiMessageDto> saveMemoryTestRanking(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int level = rankingRequestDto.getLevel();
        int score = rankingRequestDto.getGameScore();
        User user = userDetails.getUser();

        memoryTestRankingRedisService.createRanking(user, level, score);
        return ResponseEntity.ok(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 랭킹이 등록됐습니다."));
    }

    @GetMapping("/memory_test/ranking")
    public ResponseEntity<List<RankingResponseDto>> getTopMemoryTestRankings() {
        List<RankingResponseDto> rankings = memoryTestRankingRedisService.getTopRankings(TOP_RANKING);
        return ResponseEntity.ok(rankings);
    }

    @PostMapping("/snake_game/ranking")
    public ResponseEntity<ApiMessageDto> saveRSnakeGameRanking(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int level = rankingRequestDto.getLevel();
        int score = rankingRequestDto.getGameScore();
        User user = userDetails.getUser();

        snakeGameRankingRedisService.createRanking(user, level, score);
        return ResponseEntity.ok(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 랭킹이 등록됐습니다."));
    }

    @GetMapping("/snake_game/ranking")
    public ResponseEntity<List<RankingResponseDto>> getTopSnakeGameRankings() {
        List<RankingResponseDto> rankings = snakeGameRankingRedisService.getTopRankings(TOP_RANKING);
        return ResponseEntity.ok(rankings);
    }
}
