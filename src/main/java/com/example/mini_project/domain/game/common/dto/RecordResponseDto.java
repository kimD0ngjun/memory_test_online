package com.example.mini_project.domain.game.common.dto;

import com.example.mini_project.domain.game.memory.entity.MemoryTestRecord;
import com.example.mini_project.domain.game.snake.entity.SnakeGameRecord;
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

    public RecordResponseDto(MemoryTestRecord memoryTestRecord) {
        this.id = memoryTestRecord.getId();
        this.level = memoryTestRecord.getLevel();
        this.gameScore = memoryTestRecord.getGameScore();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH:mm:ss");
        this.registration = memoryTestRecord.getCreatedAt().format(formatter);
    }

    public RecordResponseDto(SnakeGameRecord snakeGameRecord) {
        this.id = snakeGameRecord.getId();
        this.level = snakeGameRecord.getLevel();
        this.gameScore = snakeGameRecord.getGameScore();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH:mm:ss");
        this.registration = snakeGameRecord.getCreatedAt().format(formatter);
    }
}
