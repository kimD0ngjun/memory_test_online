package com.example.mini_project.domain.page;

import com.example.mini_project.domain.game.dto.RankingResponseDto;
import com.example.mini_project.domain.game.service.RankingRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final RankingRedisService rankingService;
    private static final int TOP_RANKING = 10;

    @GetMapping
    public String getAuthPage() {
        return "auth";
    }

    @GetMapping("/game")
    public String getGamePage(Model model) {
        List<RankingResponseDto> rankings = rankingService.getTopRankings(TOP_RANKING);
        model.addAttribute("rankings", rankings);

        return "game";
    }

    @GetMapping("/error")
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        String errorMessage = "접근 권한이 없습니다.";
        model.addAttribute("error", errorMessage);
        return "error";
    }
}
