package com.example.mini_project.domain.game.common.controller;

import com.example.mini_project.domain.game.common.dto.RankingRequestDto;
import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.game.common.service.RankingRedisService;
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
@RequestMapping("/mini/game")
public class RankingRedisController {

    private final RankingRedisService rankingService;
    private static final int TOP_RANKING = 10;

    @PostMapping("/ranking")
    public ResponseEntity<ApiMessageDto> saveRanking(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int level = rankingRequestDto.getLevel();
        int score = rankingRequestDto.getGameScore();
        User user = userDetails.getUser();

        rankingService.createRanking(user, level, score);
        return ResponseEntity.ok(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 랭킹이 등록됐습니다."));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingResponseDto>> getTopRankings() {
        List<RankingResponseDto> rankings = rankingService.getTopRankings(TOP_RANKING);
        return ResponseEntity.ok(rankings);
    }
}
