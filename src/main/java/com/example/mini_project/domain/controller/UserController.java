package com.example.mini_project.domain.controller;

import com.example.mini_project.domain.dto.UserDto;
import com.example.mini_project.domain.service.UserService;
import com.example.mini_project.global.dto.ApiMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User 컨트롤러", description = "회원 기능을 전반적으로 담당하는 컨트롤러")
@AllArgsConstructor
@RestController
@RequestMapping("/mini/user")
public class UserController {

    private final UserService userService;

    // Add Employee(Sign up)
    @Operation(summary = "회원가입", description = "사용자 회원가입을 위한 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복 이메일 가입 시도",
                    content = @Content(schema = @Schema(implementation = ApiMessageDto.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<UserDto> createEmployee(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test success", HttpStatus.OK);
    }

    //TODO: 회원정보 RUD
//    // Get Employee
//    @GetMapping("/{userId}")
//    public ResponseEntity<UserDto> getEmployeeById(@PathVariable Long userId) {
//        UserDto employee = userService.getUserById(userId);
//        return ResponseEntity.ok(employee);
//    }
//
//
//    // Get All Employees
//    @GetMapping
//    public ResponseEntity<List<UserDto>> getAllEmployees() {
//        List<UserDto> allEmployees = userService.getAllUsers();
//        return ResponseEntity.ok(allEmployees);
//    }
//
//    // Update Employee
//    @PutMapping("/{userId}")
//    public ResponseEntity<UserDto> updateEmployee(@PathVariable Long userId,
//                                                  @RequestBody UserDto updatedEmployee) {
//        UserDto userDto = userService.updateUser(userId, updatedEmployee);
//        return ResponseEntity.ok(userDto);
//    }
//
//    // Delete Employee
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> deleteEmployee(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//        return ResponseEntity.ok(userId + "번 ID의 회원 정보가 성공적으로 삭제됐습니다.");
//    }
}
