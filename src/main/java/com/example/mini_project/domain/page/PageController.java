package com.example.mini_project.domain.page;

import com.example.mini_project.domain.game.common.dto.ProfileResponseDto;
import com.example.mini_project.domain.game.common.dto.RankingResponseDto;
import com.example.mini_project.domain.game.common.dto.RecordResponseDto;
import com.example.mini_project.domain.game.memory.service.MemoryTestRankingRedisService;
import com.example.mini_project.domain.game.memory.service.MemoryTestService;
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

    private final MemoryTestRankingRedisService rankingService;
    private final MemoryTestService memoryTestService;
    private static final int TOP_RANKING = 10;

    @GetMapping
    public String getAuthPage() {
        return "auth";
    }

    @GetMapping("/tutorial")
    public String getTutorialPage() {
        return "tutorial";
    }

    @GetMapping("/game")
    public String getGamePage(Model model) {
        List<RankingResponseDto> rankings = rankingService.getTopRankings(TOP_RANKING);
        model.addAttribute("rankings", rankings);

        return "game";
    }

    @GetMapping("/mypage")
    public String getMyPage(
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        User user = userDetails.getUser();
        List<RecordResponseDto> records = memoryTestService.getRecords(user);
        ProfileResponseDto profile = new ProfileResponseDto(user, records);

        model.addAttribute("profile", profile);
        model.addAttribute("records", records);

        log.info(profile.toString());
        log.info(records.toString());

        return "mypage";
    }

    @GetMapping("/error")
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        String errorMessage = "접근 권한이 없습니다.";
        model.addAttribute("error", errorMessage);
        return "error";
    }
}
