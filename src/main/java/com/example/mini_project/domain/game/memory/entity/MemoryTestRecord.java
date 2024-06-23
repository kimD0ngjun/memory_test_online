package com.example.mini_project.domain.game.memory.entity;

import com.example.mini_project.domain.game.common.dto.RankingRequestDto;
import com.example.mini_project.domain.game.common.util.RecordTimeStamped;
import com.example.mini_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memory_test_records")
public class MemoryTestRecord extends RecordTimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "score", nullable = false)
    private int gameScore;

    public MemoryTestRecord(User user, RankingRequestDto rankingRequestDto) {
        this.user = user;
        this.level = rankingRequestDto.getLevel();
        this.gameScore = rankingRequestDto.getGameScore();
    }
}
