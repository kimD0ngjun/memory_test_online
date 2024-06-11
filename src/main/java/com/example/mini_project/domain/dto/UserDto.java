package com.example.mini_project.domain.dto;

import com.example.mini_project.domain.entity.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String username;

    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @Schema(description = "사용자 암호")
    private String password;

    @Schema(description = "관리자 권한 인증번호(유효하게 발급받았을 경우)", nullable = true)
    private String adminNumber;

    @Schema(description = "사용자 권한(가입 당시에는 입력 x)", nullable = true, example = "ROLE_USER")
    private String role;

    public UserDto(String username, String email, UserRoleEnum role) {
        this.username = username;
        this.email = email;
        this.role = role.getRole();
    }
}
