package com.modsensoftware.book_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Указываем, что будет использоваться тестовый профиль
class BookEntityServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
