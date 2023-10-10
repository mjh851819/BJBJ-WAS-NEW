package com.service.BOOKJEOK.dto.user;

import com.service.BOOKJEOK.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {

    @Getter
    @Setter
    public static class UserSearchResDto {
        Long user_id;
        String user_name;
        String user_email;
        String img_url;


        public UserSearchResDto(User user) {
            this.user_id = user.getId();
            this.user_name = user.getName();
            this.user_email = user.getEmail();
            this.img_url = user.getImg_url();
        }
    }
}
