package com.service.BOOKJEOK.repository;

import com.service.BOOKJEOK.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<String> findByEmail(String email);
}
