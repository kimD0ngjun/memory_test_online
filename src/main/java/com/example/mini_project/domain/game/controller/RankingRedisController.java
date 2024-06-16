package com.example.mini_project.domain.game.controller;

import com.example.mini_project.domain.game.dto.RankingRequestDto;
import com.example.mini_project.domain.game.entity.Ranking;
import com.example.mini_project.domain.game.service.RankingRedisService;
import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.domain.user.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> saveRanking(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int level = rankingRequestDto.getLevel();
        int score = rankingRequestDto.getGameScore();
        User user = userDetails.getUser();

        rankingService.createRanking(user, level, score);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Ranking>> getTopRankings() {
        List<Ranking> rankings = rankingService.getTopRankings(TOP_RANKING);
        return ResponseEntity.ok(rankings);
    }
}
