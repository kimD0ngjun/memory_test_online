package com.example.mini_project.domain.game.common.controller;

import com.example.mini_project.domain.game.common.dto.RankingRequestDto;
import com.example.mini_project.domain.game.common.dto.RecordResponseDto;
import com.example.mini_project.domain.game.memory.service.MemoryTestRecordService;
import com.example.mini_project.domain.game.snake.service.SnakeGameRecordService;
import com.example.mini_project.domain.user.entity.UserDetailsImpl;
import com.example.mini_project.global.dto.ApiMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mini")
public class RecordController {

    private final MemoryTestRecordService memoryTestRecordService;
    private final SnakeGameRecordService snakeGameRecordService;

    @PostMapping("/memory_test/record")
    public ResponseEntity<ApiMessageDto> createMemoryTestRecord(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        memoryTestRecordService.createRecord(userDetails.getUser(), rankingRequestDto);
        return ResponseEntity.ok().body(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 기록이 등록됐습니다."));
    }


    @GetMapping("/memory_test/record")
    public ResponseEntity<List<RecordResponseDto>> getMemoryTestRecords(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<RecordResponseDto> records = memoryTestRecordService.getRecords(userDetails.getUser());
        return ResponseEntity.ok().body(records);
    }

    @DeleteMapping("/memory_test/record")
    public ResponseEntity<ApiMessageDto> deleteMemoryTestRecord(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id
    ) {
        memoryTestRecordService.deleteRecord(userDetails.getUser(), id);
        return ResponseEntity.ok().body(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 기록이 삭제됐습니다."));
    }

    @PostMapping("/snake_game/record")
    public ResponseEntity<ApiMessageDto> createSnakeGameRecord(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        snakeGameRecordService.createRecord(userDetails.getUser(), rankingRequestDto);
        return ResponseEntity.ok().body(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 기록이 등록됐습니다."));
    }


    @GetMapping("/snake_game/record")
    public ResponseEntity<List<RecordResponseDto>> getSnakeGameRecords(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<RecordResponseDto> records = snakeGameRecordService.getRecords(userDetails.getUser());
        return ResponseEntity.ok().body(records);
    }

    @DeleteMapping("/snake_game/record")
    public ResponseEntity<ApiMessageDto> deleteSnakeGameRecord(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id
    ) {
        snakeGameRecordService.deleteRecord(userDetails.getUser(), id);
        return ResponseEntity.ok().body(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 기록이 삭제됐습니다."));
    }
}
