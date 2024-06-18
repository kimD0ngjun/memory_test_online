package com.example.mini_project.domain.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mini/game")
public class GameController {

    @GetMapping("/play")
    public String getGamePlay() {
        return "auth";
    }
}
