package com.example.mini_project.domain.game.snake.service;

import com.example.mini_project.domain.game.common.dto.RankingRequestDto;
import com.example.mini_project.domain.game.common.dto.RecordResponseDto;
import com.example.mini_project.domain.game.common.service.RecordService;
import com.example.mini_project.domain.game.memory.entity.MemoryTestRecord;
import com.example.mini_project.domain.game.snake.entity.SnakeGameRecord;
import com.example.mini_project.domain.game.snake.repository.SnakeGameRecordRepository;
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
public class SnakeGameRecordService implements RecordService {

    private final SnakeGameRecordRepository snakeGameRecordRepository;

    @Override
    public void createRecord(User user, RankingRequestDto rankingRequestDto) {
        SnakeGameRecord snakeGameRecord = new SnakeGameRecord(user, rankingRequestDto);
        snakeGameRecordRepository.save(snakeGameRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordResponseDto> getRecords(User user) {
        return snakeGameRecordRepository.findByUser(user).stream().map(RecordResponseDto::new).toList();
    }

    @Override
    public void deleteRecord(User user, Long recordId) {
        SnakeGameRecord snakeGameRecord =
                snakeGameRecordRepository.findById(recordId).orElseThrow(
                        () -> new ResourceNotFoundException("조회하려는 기록 없음!"));

        log.info("파라미터 이메일 : " + user.getEmail());
        log.info("기록의 이메일 : " + snakeGameRecord.getUser().getEmail());

        if (!snakeGameRecord.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("본인 소유 기록 아님!");
        }

        snakeGameRecordRepository.deleteById(recordId);
    }
}
