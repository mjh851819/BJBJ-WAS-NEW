package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.User;
import com.service.BOOKJEOK.dto.user.UserResponseDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.user.UserResponseDto.*;

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
            throw new CustomApiException("유저를 찾을 수 없습니다.");
        }

        return new UserSearchResDto(userPS.get());
    }

    //OAuth로 인한 자동 회원가입 / 로그인 이기 때문에 현재는 사용되지 않음
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
