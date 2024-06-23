package com.example.mini_project.domain.game.common.controller;

import com.example.mini_project.domain.game.common.dto.RankingRequestDto;
import com.example.mini_project.domain.game.common.dto.RecordResponseDto;
import com.example.mini_project.domain.game.memory.service.MemoryTestService;
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
@RequestMapping("/mini/game")
public class RecordController {

    private final MemoryTestService memoryTestService;

    @PostMapping("/record")
    public ResponseEntity<ApiMessageDto> createRecord(
            @RequestBody RankingRequestDto rankingRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        memoryTestService.createRecord(userDetails.getUser(), rankingRequestDto);
        return ResponseEntity.ok().body(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 기록이 등록됐습니다."));
    }


    @GetMapping("/record")
    public ResponseEntity<List<RecordResponseDto>> getRecords(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<RecordResponseDto> records = memoryTestService.getRecords(userDetails.getUser());
        return ResponseEntity.ok().body(records);
    }

    @DeleteMapping("/record")
    public ResponseEntity<ApiMessageDto> deleteRecord(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long id
    ) {
        memoryTestService.deleteRecord(userDetails.getUser(), id);
        return ResponseEntity.ok().body(
                new ApiMessageDto(HttpStatus.OK.value(), "정상적으로 기록이 삭제됐습니다."));
    }
}
