package com.example.mini_project.domain.game.dto;

import com.example.mini_project.domain.game.entity.MemoryTestRanking;
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
    private int level;
    private String gameScore;

    public RankingResponseDto(MemoryTestRanking memoryTestRanking, int rank) {
        this.rank = rank + "등";
        this.username = memoryTestRanking.getUsername();
        this.email = memoryTestRanking.getEmail();
        this.level = memoryTestRanking.getLevel();
        this.gameScore = memoryTestRanking.getGameScore() + " 점";
    }
}
