package com.example.mini_project.domain.game.dto;

import com.example.mini_project.domain.game.entity.Ranking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponseDto {
    private int rank;
    private String username;
    private String email;
    private int level;
    private int gameScore;

    public RankingResponseDto(Ranking ranking, int rank) {
        this.rank = rank;
        this.username = ranking.getUsername();
        this.email = ranking.getEmail();
        this.level = ranking.getLevel();
        this.gameScore = ranking.getGameScore();
    }
}
