package com.example.mini_project.global.auth.entity;

import com.example.mini_project.domain.user.entity.UserRoleEnum;
import lombok.Getter;

import java.util.Date;

@Getter
public class TokenPayload {
    private String sub;
    private String jti;
    private String role;
    private Date iat;
    private Date expiresAt;
    private String tokenType;

    public TokenPayload(String sub, String jti, Date iat, Date expiresAt, UserRoleEnum role, TokenType type) {
        this.sub = sub;
        this.jti = jti;
        this.role = role.getRole();
        this.iat = iat;
        this.expiresAt = expiresAt;
        this.tokenType = type.getType();
    }
}
