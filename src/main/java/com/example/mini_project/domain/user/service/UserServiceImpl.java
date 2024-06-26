package com.example.mini_project.domain.user.service;

import com.example.mini_project.domain.user.dto.UserDto;
import com.example.mini_project.domain.user.entity.User;
import com.example.mini_project.global.exception.DuplicationException;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import com.example.mini_project.domain.user.mapper.UserMapper;
import com.example.mini_project.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        log.info("가입한 이메일: " + userDto.getEmail());
        log.info("이미 가입되어 있는가: " + userRepository.findByEmail(userDto.getEmail()).isPresent());

        // 이메일 중복 처리 예외
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 이메일은 이미 가입되어 있습니다");
        }

        log.info("가입한 이메일2: " + userDto.getEmail());

        User user;
        String password = passwordEncoder.encode(userDto.getPassword());

//        // TODO: 추후 관리자 인증번호 발급 등의 로직 추가 필요
//        if (userDto.getAdminNumber() != null) {
//            user = UserMapper.mapToAdmin(userDto, password);
//        } else {
//            user = UserMapper.mapToUser(userDto, password);
//        }
        user = UserMapper.mapToUser(userDto, password);

        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "조회하려는 " + userId + "번 ID의 회원이 없습니다.")
                );

        return UserMapper.mapToUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public void updateUsername(User user, UserDto updatedUser) {
        User profile = userRepository.findById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("수정하려는 회원 정보가 존재하지 않습니다")
        );

        profile.updateUser(updatedUser);
//        userRepository.save(profile);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("삭제하려는 " + userId + "번 ID가 존재하지 않습니다")
        );

        userRepository.deleteById(userId);
    }
}
