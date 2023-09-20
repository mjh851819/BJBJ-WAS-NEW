package com.service.BOOKJEOK.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor //스프링이 User 객체 생성할 때 빈 생성자로 new를 하기 때문에
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "clubs")
@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String img_url;
    private String contents;
    private int max_personnel;
    private String description;
    private int likes;
    private String status;
    private String tags;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    //ERD에 추가
    @OneToMany(mappedBy = "club")
    private final List<Member> memberList = new ArrayList<>();

    //ERD에 추가
    @OneToMany(mappedBy = "club")
    private final List<Feed> feedList = new ArrayList<>();

    @CreatedDate //insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //insert, update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Club(Long id, String title, String img_url, String contents, int max_personnel, String description, int likes, String status, String tags, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.img_url = img_url;
        this.contents = contents;
        this.max_personnel = max_personnel;
        this.description = description;
        this.likes = likes;
        this.status = status;
        this.tags = tags;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
