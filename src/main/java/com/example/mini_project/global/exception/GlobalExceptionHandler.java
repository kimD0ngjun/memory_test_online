package com.example.mini_project.global.exception;

import com.example.mini_project.global.dto.ApiMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiMessageDto> handleResourceNotFoundException(ResourceNotFoundException nfe) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiMessageDto(HttpStatus.NOT_FOUND.value(), nfe.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ApiMessageDto> handleDuplicationException(DuplicationException de) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiMessageDto(HttpStatus.CONFLICT.value(), de.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ApiMessageDto> handleIllegalArgumentException(IllegalArgumentException iae) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiMessageDto(HttpStatus.BAD_REQUEST.value(), iae.getMessage()));
    }
}
