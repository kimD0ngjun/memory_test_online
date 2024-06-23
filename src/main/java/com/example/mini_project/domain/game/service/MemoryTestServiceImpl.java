package com.example.mini_project.domain.game.service;

import com.example.mini_project.domain.game.dto.RankingRequestDto;
import com.example.mini_project.domain.game.dto.RecordResponseDto;
import com.example.mini_project.domain.game.entity.MemoryTestRecord;
import com.example.mini_project.domain.game.repository.MemoryTestRecordRepository;
import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemoryTestServiceImpl implements MemoryTestService {

    private final MemoryTestRecordRepository memoryTestRecordRepository;

    @Override
    public void createRecord(User user, RankingRequestDto rankingRequestDto) {
        MemoryTestRecord memoryTestRecord = new MemoryTestRecord(user, rankingRequestDto);
        memoryTestRecordRepository.save(memoryTestRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordResponseDto> getRecords(User user) {
        return memoryTestRecordRepository.findByUser(user).stream().map(RecordResponseDto::new).toList();
    }

    @Override
    public void deleteRecord(User user, Long recordId) {
        MemoryTestRecord memoryTestRecord =
                memoryTestRecordRepository.findById(recordId).orElseThrow(
                        () -> new ResourceNotFoundException("조회하려는 기록 없음!"));

        log.info("파라미터 이메일 : " + user.getEmail());
        log.info("기록의 이메일 : " + memoryTestRecord.getUser().getEmail());

        if (!memoryTestRecord.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("본인 소유 기록 아님!");
        }

        memoryTestRecordRepository.deleteById(recordId);
    }
}
