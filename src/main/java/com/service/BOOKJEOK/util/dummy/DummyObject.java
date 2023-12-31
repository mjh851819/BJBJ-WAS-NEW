package com.service.BOOKJEOK.util.dummy;

import com.service.BOOKJEOK.domain.Comment;
import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.domain.user.UserEnum;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;

import java.time.LocalDateTime;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;

public class DummyObject {

    protected User newUser(String name, String email) {
        return User.builder()
                .name(name)
                .email(email)
                .role(UserEnum.USER)
                .build();
    }

    protected User newMockUser(Long id, String name, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(UserEnum.USER)
                .build();
    }

    protected Club newClub(String title, User user) {
        return Club.builder()
                .user(user)
                .title(title)
                .tags("온라인,오프라인")
                .build();
    }

    protected Club newMockClub(Long id, String title, User user) {
        return Club.builder()
                .user(user)
                .id(id)
                .title(title)
                .tags("온라인,오프라인")
                .build();
    }


    protected Club newJpaClub(Long id, String title, User user) {
        return Club.builder()
                .user(user)
                .id(id)
                .title(title)
                .tags("온라인,오프라인")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Feed newFeed(String title, User user, Club club) {
        return Feed.builder()
                .club(club)
                .user(user)
                .title(title)
                .img_url("url")
                .contents("contents")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Comment newComment(String contents, User user, Feed feed) {
        return Comment.builder()
                .user(user)
                .feed(feed)
                .contents(contents)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected LikedFeed newLikedFeed(User user, Feed feed) {
        return LikedFeed.builder()
                .user(user)
                .feed(feed)
                .build();
    }
}
