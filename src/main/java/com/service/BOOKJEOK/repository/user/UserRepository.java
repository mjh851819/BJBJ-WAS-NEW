package com.service.BOOKJEOK.repository.user;

import com.service.BOOKJEOK.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    Optional<User> findByRefresh(String token);
}
