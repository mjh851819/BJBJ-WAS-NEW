package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.user.UserResponseDto.*;
import static com.service.BOOKJEOK.handler.ex.ExMessage.NOT_FOUND_USER;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserSearchResDto searchUser(Long userId) {
        Optional<User> userPS = userRepository.findById(userId);

        if(userPS.isEmpty()){
            throw new CustomApiException(NOT_FOUND_USER);
        }

        return new UserSearchResDto(userPS.get());
    }

    //OAuth로 인한 자동 회원가입 / 로그인 이기 때문에 현재는 사용되지 않음
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
