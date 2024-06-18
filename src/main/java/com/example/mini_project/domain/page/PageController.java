package com.example.mini_project.domain.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping
    public String getAuthPage() {
        return "auth";
    }

    @GetMapping("/game")
    public String getGamePage() {
        return "game";
    }

    @GetMapping("/error")
    public String showEditError(@ModelAttribute("error") String error, Model model) {
        String errorMessage = "접근 권한이 없습니다.";
        model.addAttribute("error", errorMessage);
        return "error";
    }
}
