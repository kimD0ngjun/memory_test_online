package com.example.mini_project.global.auth.controller;

import com.example.mini_project.domain.user.entity.UserDetailsImpl;
import com.example.mini_project.global.auth.service.AuthService;
import com.example.mini_project.global.dto.ApiMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth 컨트롤러", description = "회원 기능 중 토큰 처리를 담당하는 컨트롤러")
@AllArgsConstructor
@RestController
@RequestMapping("/mini/user")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그아웃", description = "회원 로그아웃을 위한 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "비정상적인 로그아웃 시도",
                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiMessageDto> createEmployee(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiMessageDto apiMessageDto = authService.logout(userDetails.getUsername());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.OK);
    }

}
