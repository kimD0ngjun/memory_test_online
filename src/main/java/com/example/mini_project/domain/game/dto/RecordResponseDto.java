package com.example.mini_project.domain.game.dto;

import com.example.mini_project.domain.game.entity.Record;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordResponseDto {
    private int level;
    private int gameScore;
    private String registration;

    public RecordResponseDto(Record record) {
        this.level = record.getLevel();
        this.gameScore = record.getGameScore();
        this.registration = record.getCreatedAt().toString();
    }
}
