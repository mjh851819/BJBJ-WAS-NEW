package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.User;
import com.service.BOOKJEOK.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User searchUser(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
