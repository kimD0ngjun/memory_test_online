package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.user.entity.UserRoleEnum;
import com.example.mini_project.global.auth.entity.TokenPayload;
import com.example.mini_project.global.auth.entity.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {
    // Request에서 받을 KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Response에 담을 KEY 값
    public static final String TOKEN_TYPE = "type";
    public static final String ACCESS_TOKEN_HEADER = "AccessToken";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // Access 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    // Refresh 토큰 만료시간
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 7 * 1000L; // 7일

    private final SecretKey secretKey;

    // 로그 세팅
    public static final Logger logger = LoggerFactory.getLogger("jwt 발급 및 처리 로직");

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 토큰 생성을 위한 페이로드 생성 메소드
    public TokenPayload createTokenPayload(String email, UserRoleEnum role, TokenType tokenType)  {
        Date date = new Date();
        long tokenTime = TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;

        return new TokenPayload(
                email,
                UUID.randomUUID().toString(),
                date,
                new Date(date.getTime() + tokenTime),
                role,
                tokenType
        );
    }

    // 시간을 동일하게 맞춰주기 위한 동시 생성 메소드
    // 인덱스 0: accessTokenPayload, 인덱스 1: refreshTokenPayload
    public List<TokenPayload> createTokenPayloads(String email, UserRoleEnum role) {
        List<TokenPayload> tokenPayloads = new ArrayList<>();
        Date date = new Date();

        TokenPayload accessTokenPayload = new TokenPayload(
                email,
                UUID.randomUUID().toString(),
                date,
                new Date(date.getTime() + ACCESS_TOKEN_TIME),
                role,
                TokenType.ACCESS
        );

        TokenPayload refreshTokenPayload = new TokenPayload(
                email,
                UUID.randomUUID().toString(),
                date,
                new Date(date.getTime() + REFRESH_TOKEN_TIME),
                role,
                TokenType.REFRESH
        );

        tokenPayloads.add(accessTokenPayload);
        tokenPayloads.add(refreshTokenPayload);

        return tokenPayloads;
    }

    // 11.5 -> 12.3
    public String createAccessToken(TokenPayload payload) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(payload.getSub()) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, payload.getRole()) // 사용자 권한
                        .expiration(payload.getExpiresAt()) // 만료 시간
                        .issuedAt(payload.getIat()) // 발급일
                        .id(payload.getJti()) // JWT ID
                        .claim(TOKEN_TYPE, payload.getTokenType())
                        .signWith(secretKey) // 암호화 Key & 알고리즘
                        .compact();
    }

    public String createRefreshToken(TokenPayload payload) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(payload.getSub()) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, payload.getRole()) // 사용자 권한
                        .expiration(payload.getExpiresAt()) // 만료 시간
                        .issuedAt(payload.getIat()) // 발급일
                        .id(payload.getJti()) // JWT ID
                        .claim(TOKEN_TYPE, payload.getTokenType())
                        .signWith(secretKey) // 암호화 Key & 알고리즘
                        .compact();
    }

    public Date getTokenIat(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getIssuedAt();
    }

    // cookie에 리프레시 토큰 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // jwt 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }

        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().
                    verifyWith(secretKey).
                    build().
                    parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명입니다.");
            throw new JwtException("Invalid JWT signature, 유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT 토큰입니다.");
            throw new JwtException("Expired JWT token, 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
            throw new JwtException("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
            throw new JwtException("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            logger.error("기타 에러 확인 요망");
        }

        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 쿠키에서 엑세스 토큰 갖고오기
    public String getAccessTokenFromRequestCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(ACCESS_TOKEN_HEADER)) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); // Encode 되어 넘어간 Value 다시 Decode
                }
            }
        }
        return null;
    }

    // 쿠키에서 리프레쉬 토큰 갖고오기
    public String getRefreshTokenFromRequestCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_HEADER)) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); // Encode 되어 넘어간 Value 다시 Decode
                }
            }
        }
        return null;
    }


    //TODO: 만약 헤더에서 곧장 바로 가져온다면 이 메소드를 변형해야 할듯
    public String getJwtFromRequestHeader(HttpServletRequest request, TokenType tokenType) {
        String bearerToken = request.getHeader(TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_HEADER : REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
