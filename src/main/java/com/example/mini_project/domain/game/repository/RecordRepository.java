package com.example.mini_project.domain.game.repository;

import com.example.mini_project.domain.game.entity.Record;
import com.example.mini_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByUser(User user);
}
