package com.example.mini_project.domain.game.entity;

import com.example.mini_project.domain.game.dto.RankingRequestDto;
import com.example.mini_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "records")
public class Record extends RecordTimeStamped {

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

    public Record(User user, RankingRequestDto rankingRequestDto) {
        this.user = user;
        this.level = rankingRequestDto.getLevel();
        this.gameScore = rankingRequestDto.getGameScore();
    }
}
