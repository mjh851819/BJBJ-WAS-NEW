package com.service.BOOKJEOK.dto.user;

import com.service.BOOKJEOK.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {

    @Getter
    @Setter
    public static class UserSearchResDto {
        Long userId;
        String userName;
        String userEmail;
        String imgUrl;


        public UserSearchResDto(User user) {
            this.userId = user.getId();
            this.userName = user.getName();
            this.userEmail = user.getEmail();
            this.imgUrl = user.getImg_url();
        }
    }
}
