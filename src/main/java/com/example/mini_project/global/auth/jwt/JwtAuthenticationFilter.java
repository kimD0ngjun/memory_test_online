package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.dto.UserLoginRequestDto;
import com.example.mini_project.domain.dto.UserLoginResponseDto;
import com.example.mini_project.domain.entity.UserDetailsImpl;
import com.example.mini_project.domain.entity.UserRoleEnum;
import com.example.mini_project.domain.repository.UserRepository;
import com.example.mini_project.global.auth.entity.TokenPayload;
import com.example.mini_project.global.auth.entity.TokenType;
import com.example.mini_project.global.exception.DuplicationException;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import com.example.mini_project.global.redis.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j(topic = "로그인 및 JWT 생성 + 인증")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil; // 로그인 성공 시, 존맛탱 발급을 위한 의존성 주입
    private final UserRepository userRepository;
    private final RedisUtils redisUtils;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository, RedisUtils redisUtils) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/mini/user/login"); // 로그인 처리 경로 설정(매우매우 중요)
        super.setUsernameParameter("email");
        this.userRepository = userRepository;
        this.redisUtils = redisUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 로그인 시도를 담당함
        log.info("로그인 단계 진입");

        try {
            UserLoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDto.class);
            return getAuthenticationManager().authenticate( // 인증 처리 하는 메소드
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        // 신 버전
        // 인덱스 0: accessTokenPayload, 인덱스 1: refreshTokenPayload
        List<TokenPayload> tokenPayloads = jwtUtil.createTokenPayloads(username, role);

        String accessToken = jwtUtil.createAccessToken(tokenPayloads.get(0));
        String refreshToken = jwtUtil.createRefreshToken(tokenPayloads.get(1));

        userRepository.findByEmail(username).orElseThrow(
                () ->  new ResourceNotFoundException("데이터베이스의 이메일 정보와 서버의 이메일 정보가 다름.")
        );

        if (redisUtils.getData(username) != null) {
            throw new DuplicationException("이미 로그인되어있는 사용자! 공격자 확인 요망");
        }

//        String accessTokenValue = accessToken.substring(7);
        String refreshTokenValue = refreshToken.substring(7);
        log.info("초기 리프레쉬토큰: " + refreshTokenValue);

        // username(email) - refreshToken 덮어씌우기 저장
        redisUtils.setData(username, refreshTokenValue);

        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, accessToken);
        jwtUtil.addJwtToCookie(refreshToken, response);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(new UserLoginResponseDto("로그인 성공 및 토큰 발급"));
        PrintWriter writer = response.getWriter();

        writer.print(jsonMessage);
        writer.flush();
        writer.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패 및 401 에러 반환");

        // 로그인 실패로 상태코드 반환
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(new UserLoginResponseDto("로그인 실패"));
        PrintWriter writer = response.getWriter();

        writer.print(jsonMessage);
        writer.flush();
        writer.close();
    }
}
