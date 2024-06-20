package com.example.mini_project.domain.game.dto;

import com.example.mini_project.domain.game.entity.Record;
import com.example.mini_project.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    String email;
    String username;
    String averageLevel;
    String averageScore;

    public ProfileResponseDto(User user, List<RecordResponseDto> records) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.averageLevel = calculateAverageLevel(records) + " 단계";
        this.averageScore = calculateAverageScore(records) + "점 ";
    }

    private double calculateAverageLevel(List<RecordResponseDto> records) {
        return records.stream()
                .mapToDouble(RecordResponseDto::getLevel)
                .average()
                .orElse(0.0);
    }

    private double calculateAverageScore(List<RecordResponseDto> records) {
        return records.stream()
                .mapToDouble(RecordResponseDto::getGameScore)
                .average()
                .orElse(0.0);
    }
}
