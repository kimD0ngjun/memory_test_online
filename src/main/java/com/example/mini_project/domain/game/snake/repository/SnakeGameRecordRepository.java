package com.example.mini_project.domain.game.snake.repository;

import com.example.mini_project.domain.game.memory.entity.MemoryTestRecord;
import com.example.mini_project.domain.game.snake.entity.SnakeGameRecord;
import com.example.mini_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnakeGameRecordRepository extends JpaRepository<SnakeGameRecord, Long> {
    List<SnakeGameRecord> findByUser(User user);
}
