package com.service.BOOKJEOK.userService;

import com.service.BOOKJEOK.domain.User;
import com.service.BOOKJEOK.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
