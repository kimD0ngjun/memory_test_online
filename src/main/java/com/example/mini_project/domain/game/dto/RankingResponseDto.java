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
    private String rank;
    private String username;
    private String email;
    private String level;
    private String gameScore;

    public RankingResponseDto(Ranking ranking, int rank) {
        this.rank = rank + "등";
        this.username = ranking.getUsername();
        this.email = ranking.getEmail();
        this.level = ranking.getLevel() + " 단계";
        this.gameScore = ranking.getGameScore() + " 점";
    }
}
