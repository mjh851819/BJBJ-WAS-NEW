package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.util.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.user.UserResponseDto.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void searchUser_Test() throws Exception {
        //given
        Long id = 1L;

        //when
        User user = newMockUser(id, "juhong", "mjh8518@naver.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserSearchResDto userSearchResDto = userService.searchUser(id);

        //then
        Assertions.assertThat(userSearchResDto.getUserId()).isEqualTo(id);
        Assertions.assertThat(userSearchResDto.getUserName()).isEqualTo(user.getName());
        Assertions.assertThat(userSearchResDto.getUserEmail()).isEqualTo(user.getEmail());
    }

}