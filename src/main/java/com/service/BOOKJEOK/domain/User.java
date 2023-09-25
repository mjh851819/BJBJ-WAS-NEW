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
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 40)
    private String email;

    private String img_url;

    private String provider; // oauth로 로그인한 사용자를 구별하기 위한 필드
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role; //ADMIN, CUSTOMER

    private String refresh;

    @CreatedDate //insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //insert, update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String name, String email, String img_url, String provider, String providerId, UserEnum role, String refresh_token, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img_url = img_url;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.refresh = refresh_token;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User update(String name, String img_url){
        this.name = name;
        this.img_url = img_url;

        return this;
    }

    public void setRefreshToken(String refreshToken) {
        this.refresh = refreshToken;
    }
}
