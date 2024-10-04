package com.modsensoftware.book_service.repositories;

import com.modsensoftware.book_service.models.JwtRefreshToken;
import com.modsensoftware.book_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    @Transactional
    void deleteIfExistsByUser(User user);
}
