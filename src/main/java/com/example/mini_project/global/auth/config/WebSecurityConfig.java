package com.example.mini_project.global.auth.config;

import com.example.mini_project.domain.user.repository.UserRepository;
import com.example.mini_project.global.auth.exception.JwtAccessDenyHandler;
import com.example.mini_project.global.auth.exception.JwtAuthenticationEntryPoint;
import com.example.mini_project.global.auth.jwt.JwtAuthenticationFilter;
import com.example.mini_project.global.auth.jwt.JwtAuthorizationFilter;
import com.example.mini_project.global.auth.jwt.JwtExceptionFilter;
import com.example.mini_project.global.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisRefreshToken;
    // 필터단 예외
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 예외 커스텀 메시지 던지기
    private final JwtAccessDenyHandler jwtAccessDenyHandler; // 인가 예외 커스텀 메시지 던지기(역할별 접근권한같은)
    private final JwtExceptionFilter jwtExceptionFilter;

    // 인증 매니저 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception { // 인증필터 생성
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRepository, redisRefreshToken);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration)); // 인증매니저 설정
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() { // 인가필터 생성
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, userRepository, redisRefreshToken);
    }

    // requestMatcher 동작 불능 에러 극복을 위한 임시 빈 등록
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(new AntPathRequestMatcher("/mini/user/signup"))
//                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
//                .requestMatchers(new AntPathRequestMatcher("/css/**"))
//                .requestMatchers(new AntPathRequestMatcher("/js/**"));
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF(사이트 간 요청 위조) 설정 비활성화
        // 해당 기능은 람다식으로
        http.csrf(AbstractHttpConfigurer::disable);

        // Security 의 기본 설정인 Session 방식이 아닌 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // suhwan
//        http.cors(Customizer.withDefaults())
//                        .authorizeHttpRequests((request) ->{
//                            request.requestMatchers(HttpMethod.POST, "/mini/user/signup").permitAll();
//                        });

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("swagger-ui/**", "/v3/**").permitAll() // Swagger 관련
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 얘가 허용하는 건 static 내의 css와 js 폴더 뿐인건가?
                        .requestMatchers(HttpMethod.POST, "/mini/user/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리 요구
        );

        http.exceptionHandling(e ->
                e.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDenyHandler));

        // JWT 방식의 REST API 서버이기 때문에 FormLogin 방식, HttpBasic 방식 비활성화(논의 필요)
        // 로그인 사용
        http.formLogin((formLogin) ->
                formLogin
                        // 로그인 View 제공 (GET /api/user/login-page)
                        .loginPage("/")
                        // 로그인 처리 (POST /api/user/login)
                        .loginProcessingUrl("/mini/user/login") // 둘을 똑같이 작성하면 안 됨
                        // 로그인 처리 후 성공 시 URL
                        .defaultSuccessUrl("/game")
                        // 로그인 처리 후 실패 시 URL
                        .failureUrl("/")
                        .permitAll()
        );

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class); // 인가 처리 필터
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 인증(+ 로그인) 처리 필터
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class); // JwtAuthenticationFilter 앞단에 JwtExceptionFilter를 위치시키겠다는 설정

        return http.build();
    }
}
