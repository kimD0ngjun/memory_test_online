package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.dto.RankingRequestDto;
import com.example.mini_project.domain.game.dto.RecordResponseDto;
import com.example.mini_project.domain.game.entity.Record;
import com.example.mini_project.domain.game.repository.RecordRepository;
import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    @Override
    public void createRecord(User user, RankingRequestDto rankingRequestDto) {
        Record record = new Record(user, rankingRequestDto);
        recordRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordResponseDto> getRecords(User user) {
        return recordRepository.findByUser(user).stream().map(RecordResponseDto::new).toList();
    }

    @Override
    public void deleteRecord(User user, Long recordId) {
        Record record =
                recordRepository.findById(recordId).orElseThrow(
                        () -> new ResourceNotFoundException("조회하려는 기록 없음!"));

        if (!record.getUser().equals(user)) {
            throw new IllegalArgumentException("본인 소유 기록 아님!");
        }

        recordRepository.deleteById(recordId);
    }
}
