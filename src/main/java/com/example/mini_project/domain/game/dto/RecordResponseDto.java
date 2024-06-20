package com.example.mini_project.domain.game.dto;

import com.example.mini_project.domain.game.entity.Record;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RecordResponseDto {
    private Long id;
    private int level;
    private int gameScore;
    private String registration;

    public RecordResponseDto(Record record) {
        this.id = record.getId();
        this.level = record.getLevel();
        this.gameScore = record.getGameScore();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH:mm:ss");
        this.registration = record.getCreatedAt().format(formatter);
    }
}
