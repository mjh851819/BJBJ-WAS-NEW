package com.service.BOOKJEOK.domain;

import com.service.BOOKJEOK.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor //스프링이 User 객체 생성할 때 빈 생성자로 new를 하기 때문에
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @CreatedDate //insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //insert, update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Comment(Long id, String contents, User user, Feed feed, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.contents = contents;
        this.user = user;
        this.feed = feed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        feed.getCommentList().add(this);
    }

    public void update(String contents){
        this.contents = contents;
        this.updatedAt = LocalDateTime.now();
    }
}
