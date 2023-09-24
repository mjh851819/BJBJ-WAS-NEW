package com.service.BOOKJEOK.repository;

import com.service.BOOKJEOK.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
