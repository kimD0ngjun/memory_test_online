package com.example.mini_project.domain.service;

import com.example.mini_project.domain.dto.UserDto;
import com.example.mini_project.domain.entity.User;
import com.example.mini_project.global.exception.DuplicationException;
import com.example.mini_project.global.exception.ResourceNotFoundException;
import com.example.mini_project.domain.mapper.UserMapper;
import com.example.mini_project.domain.repository.UserRepository;
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

        // 이메일 중복 처리 예외
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicationException("해당 이메일은 이미 가입되어 있습니다");
        }

        User user;
        String password = passwordEncoder.encode(userDto.getPassword());

        // TODO: 추후 관리자 인증번호 발급 등의 로직 추가 필요
        if (userDto.getAdminNumber() != null) {
            user = UserMapper.mapToAdmin(userDto, password);
        } else {
            user = UserMapper.mapToUser(userDto, password);
        }

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
    public UserDto updateUser(Long userId, UserDto updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("수정하려는 " + userId + "번 ID가 존재하지 않습니다")
        );

        user.updateUser(updatedUser);

        User updatedUserObj = userRepository.save(user);

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("삭제하려는 " + userId + "번 ID가 존재하지 않습니다")
        );

        userRepository.deleteById(userId);
    }
}
