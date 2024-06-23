package com.example.mini_project.domain.game.repository;

import com.example.mini_project.domain.game.entity.MemoryTestRecord;
import com.example.mini_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryTestRecordRepository extends JpaRepository<MemoryTestRecord, Long> {
    List<MemoryTestRecord> findByUser(User user);
}
