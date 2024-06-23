package com.example.mini_project.domain.user.dto;

import com.example.mini_project.domain.user.entity.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "빈 칸 혹은 공백으로 이뤄진 닉네임은 허용하지 않습니다")
    @Size(min = 2, max = 10, message = "최소 2자, 최대 10자까지 닉네임을 작성합니다")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String username;

    @Email(message = "이메일 양식을 지켜주세요")
    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @Size(min = 2, message = "최소 2자 이상의 비밀번호를 작성하세요")
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
