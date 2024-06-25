package com.example.mini_project.domain.user.entity;

import com.example.mini_project.domain.game.memory.entity.MemoryTestRecord;
import com.example.mini_project.domain.game.snake.entity.SnakeGameRecord;
import com.example.mini_project.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false)
    private UserRoleEnum role;

    /**
     * 캐싱 처리할 때 지연 로딩 이슈 계속 발생... 흠....
    * */
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MemoryTestRecord> memoryTestRecords;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SnakeGameRecord> snakeGameRecords;

    public User(String username, String email, String password, UserRoleEnum role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void updateUser(UserDto userDto) {
        this.username = userDto.getUsername();
    }
}
