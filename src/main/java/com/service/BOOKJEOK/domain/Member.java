package com.service.BOOKJEOK.domain;

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
@Table(name = "members")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @CreatedDate //insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //insert, update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Member(Long id, User user, Club club, ApprovalStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.club = club;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}