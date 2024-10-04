package com.modsensoftware.book_service.repositories;

import com.modsensoftware.book_service.models.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @NonNull Optional<User> findById(@NonNull Long id);
}
