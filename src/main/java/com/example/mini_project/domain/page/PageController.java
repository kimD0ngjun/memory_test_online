package com.example.mini_project.domain.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping
    public String getAuthPage() {
        return "auth";
    }

    @GetMapping("/mini/game/play")
    public String getGamePage() {
        return "game";
    }
}
