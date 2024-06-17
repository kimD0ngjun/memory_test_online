package com.example.mini_project.global.auth.jwt;

import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.domain.user.repository.UserRepository;
import com.example.mini_project.global.auth.entity.TokenPayload;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService; // 사용자가 있는지 확인
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisRefreshToken;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        /**
         1. 클라이언트는 엑세스토큰만 신경쓴다
         2. 어차피 레디스에 리프레쉬토큰이 저장되어있다
         3. 즉 엑세스토큰을 set-cookie로 설정하면 굳이 클라이언트 측에서 토큰 관련 로직을 건드릴 필요 없다
         */

        String accessTokenValue = jwtUtil.getAccessTokenFromRequestCookie(request); // -> 요 놈을 써야 해
        log.info("쿠키로부터 갖고 온 엑세스토큰: " + accessTokenValue);
        String accessToken = jwtUtil.substringToken(accessTokenValue);

        /**

         신버전
         1. 쿠키로부터 엑세스 토큰을 갖고온다
         2. if (StringUtils.hasText(accessToken))
         3. 시간 만료를 제외한 유효성 검사부터 먼저 수행한다.
         4. 유효성 검사 통과하면 시간 만료 여부를 확인한다.
         5. 시간이 만료됐을 때, 리프레쉬토큰과의 비교 작업 처리해서 둘을 재발급한다.

         */

        // 엑세스토큰에서 이메일 정보 추출(만료됐든 아니든)
        String email = jwtUtil.getUsernameFromExpiredToken(accessToken);

        // 이메일로부터 회원 객체 조회
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("비정상적인 이메일 정보. 재확인 바람.")
        );

        // 이메일로 기존의 리프레쉬토큰 조회
        // redis에 저장된 리프레쉬토큰 갖고오기
        String refreshToken = redisRefreshToken.opsForValue().get(email);

        // 리프레쉬 토큰 만료 확인
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new ResourceNotFoundException("리프레쉬 토큰 만료, 로그아웃 요망.");
        }

        // 데이터베이스에 저장되어있는지 확인하기
        if (refreshToken == null) {
            throw new ResourceNotFoundException("저장되지 않은 토큰 정보. 공격자 확인 바람.");
        }

        if (StringUtils.hasText(accessToken)) {

            // 날짜 만료 확인
            // 만약 엑세스토큰이 만료됐으면
            // 새로운 액세스토큰과 리프레쉬토큰을 발급해야 됨
            if (jwtUtil.isTokenExpired(accessToken)) {

                log.info("토큰이 만료됨");
                // 위에까지 전부 통과됐으면 이제 엑세스토큰과 리프레쉬토큰 갱신
                // 인덱스 0: accessTokenPayload, 인덱스 1: refreshTokenPayload
                List<TokenPayload> tokenPayloads = jwtUtil.createTokenPayloads(user.getEmail(), user.getRole());

                String newAccessToken = jwtUtil.createAccessToken(tokenPayloads.get(0));
                String newRefreshToken = jwtUtil.createRefreshToken(tokenPayloads.get(1));

                log.info("새로운 엑세스토큰: " + newAccessToken);
                log.info("새로운 리프레쉬토큰: " + newRefreshToken);

                // 새로운 리프레쉬토큰 업데이트 저장
                redisRefreshToken.opsForValue().set(email, newRefreshToken.substring(7));

                try {
                    // username 담아주기
                    setAuthentication(email);
                    // 리스폰스 쿠키에 새로운 엑세스토큰 담기
                    jwtUtil.addJwtToCookie(newAccessToken, response);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            } else {
                /**
                 * 여기선 리프레쉬토큰 만료 여부 확인 불필요
                 * 엑세스토큰이 만료되지 않았다는 건 리프레쉬토큰도 만료되지 않았으므로
                 * */
                // 만약 재발급이 필요없으면 그냥 블랙리스트 여부만 판별 및 확인
                // 발급일자 비교를 통한 블랙리스트 여부 확인
                Date iatAccessToken = jwtUtil.getTokenIat(accessToken);
                Date iatRefreshToken = jwtUtil.getTokenIat(refreshToken);

                log.info("엑세스토큰 발급시간: " + iatAccessToken);
                log.info("리프레쉬토큰 발급시간: " + iatRefreshToken);

                if (!iatRefreshToken.equals(iatAccessToken)) {
                    throw new ResourceNotFoundException("블랙리스트 처리된 리프레쉬 토큰 요구 확인.");
                }

                try {
                    // username 담아주기
                    setAuthentication(email);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            }
            // 날짜 만료 제외한 나머지 엑세스 토큰 자체 유효성 판별은 이미 JwtException 필터에서 수행
        }

        // 다음 필터로 넘어가라는 의미
        // 이걸 이용해서 리프레쉬 토큰에 대한 로직 짜면 될 것 같은데
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) { // Authentication 인증 객체 만듦
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // UPAT 생성
    }
}
