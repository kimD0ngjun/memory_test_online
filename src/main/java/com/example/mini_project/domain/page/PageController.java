package com.example.mini_project.domain.page;

import com.example.mini_project.domain.game.common.dto.ProfileResponseDto;
import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.game.common.dto.RecordResponseDto;
import com.example.mini_project.domain.game.memory.service.MemoryTestRankingRedisService;
import com.example.mini_project.domain.game.memory.service.MemoryTestRecordService;
import com.example.mini_project.domain.game.snake.service.SnakeGameRankingRedisService;
import com.example.mini_project.domain.game.snake.service.SnakeGameRecordService;
import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.domain.user.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PageController {

    private final MemoryTestRankingRedisService memoryTestRankingRedisService;
    private final SnakeGameRankingRedisService snakeGameRankingRedisService;
    private final MemoryTestRecordService memoryTestRecordService;
    private final SnakeGameRecordService snakeGameRecordService;
    private static final int TOP_RANKING = 10;

    @GetMapping
    public String getAuthPage() {
        return "auth";
    }

    @GetMapping("/games")
    public String getGameSelectPage() {
        return "games";
    }

    @GetMapping("/memory_test")
    public String getMemoryTestPage(Model model) {
        List<RankingResponseDto> rankings = memoryTestRankingRedisService.getTopRankings(TOP_RANKING);
        model.addAttribute("rankings", rankings);

        return "memory_test";
    }

    @GetMapping("/snake_game")
    public String getSnakeGamePage(Model model) {
        List<RankingResponseDto> rankings = snakeGameRankingRedisService.getTopRankings(TOP_RANKING);
        model.addAttribute("rankings", rankings);

        return "snake_game";
    }

    @GetMapping("/memory_test/mypage")
    public String getMemoryTestMyPage(
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        User user = userDetails.getUser();
        List<RecordResponseDto> records = memoryTestRecordService.getRecords(user);
        ProfileResponseDto profile = new ProfileResponseDto(user, records);

        model.addAttribute("profile", profile);
        model.addAttribute("records", records);

        log.info(profile.toString());
        log.info(records.toString());

        return "memory_test_mypage";
    }

    @GetMapping("/snake_game/mypage")
    public String getSnakeGameMyPage(
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        List<RecordResponseDto> records = snakeGameRecordService.getRecords(user);
        ProfileResponseDto profile = new ProfileResponseDto(user, records);

        model.addAttribute("profile", profile);
        model.addAttribute("records", records);

        log.info(profile.toString());
        log.info(records.toString());

        return "snake_game_mypage";
    }

    @GetMapping("/error")
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        String errorMessage = "접근 권한이 없습니다.";
        model.addAttribute("error", errorMessage);
        return "error";
    }
}
