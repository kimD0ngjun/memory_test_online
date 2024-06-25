package com.example.mini_project.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
//@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 인식할 수 없는 필드를 무시하도록 설정
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @JsonCreator
    public UserDetailsImpl(@JsonProperty("user") User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 스프링 시큐리티 에서는 Username 이라는게 '식별자' 라는 의미로 쓰인다. 회원 이름이 아니다.
    }

    @Override
    @JsonIgnore // 권한은 런타임 때 동적으로 생성(굳이 역직렬화 불필요)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum userRoleEnum = user.getRole(); // 유저 권한(열거형 선언)을 UserAuthority 인스턴스에 담음
        String role = userRoleEnum.getRole(); // 그 권한들을 String 값으로 가져와서 문자열 저장

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role); // 권한값을 담은 SimpleGrantedAuthority 인스턴스
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority); // 권한을 simpleGrantedAuthority로 추상화하여 관리함(아까 말했던 권한에 따른 요청 처리를 위해서)

        return authorities; // 권한'들'이 저장된(아마 요청에 따른 뽑혀지는 권한들을 저장하는 리스트일듯?) 리스트 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
