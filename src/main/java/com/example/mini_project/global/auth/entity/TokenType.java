package com.example.mini_project.global.auth.entity;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS(Type.ACCESS),
    REFRESH(Type.REFRESH);

    private final String type;

    TokenType(String type) {
        this.type = type;
    }

    public static class Type {
        public static final String ACCESS = "TOKEN_ACCESS";
        public static final String REFRESH = "TOKEN_REFRESH";
    }
}
