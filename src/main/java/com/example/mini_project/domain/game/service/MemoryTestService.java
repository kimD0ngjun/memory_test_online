package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.dto.RankingRequestDto;
import com.example.mini_project.domain.game.dto.RecordResponseDto;
import com.example.mini_project.domain.user.entity.User;

import java.util.List;

public interface MemoryTestService {

    void createRecord(User user, RankingRequestDto rankingRequestDto);

    List<RecordResponseDto> getRecords(User user);

    void deleteRecord(User user, Long recordId);
}
