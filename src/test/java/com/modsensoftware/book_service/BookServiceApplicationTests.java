package com.modsensoftware.book_service;

import com.modsensoftware.book_service.annotations.ContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ContainerTest
@ActiveProfiles("test")
class BookServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
