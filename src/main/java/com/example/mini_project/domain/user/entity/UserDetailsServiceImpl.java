package com.example.mini_project.domain.user.entity;

import com.example.mini_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { // 얘의 목적은 입력된 username(여기서는 이메일)로 DB를 조회해서 일치하는 엔티티를 반환하기 위함

    private final UserRepository userRepository;

    @Override
    // 아까 Authentication 인증 객체에 담을 Principal에 해당하는 UserDetails 생성
    @Cacheable(
            value = "cache",
            key = "#email",
            cacheManager = "redisCacheManager",
            unless = "#result == null")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("캐시 적용이 안 될 때 호출될 이메일: {}", email);

        User user = userRepository.findByEmail(email) // 입력받은 이메일로 DB에서 유저 엔티티 조회
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));

        return new UserDetailsImpl(user);
    }
}
