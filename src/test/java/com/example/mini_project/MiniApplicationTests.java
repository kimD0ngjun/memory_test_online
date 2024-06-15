package com.example.mini_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// ./gradlew build 실패 극복을 위한 classes 속성 할당
@SpringBootTest(classes = MiniApplicationTests.class)
class MiniApplicationTests {

    @Test
    void contextLoads() {
    }

}
