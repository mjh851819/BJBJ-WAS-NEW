package com.service.BOOKJEOK.domain.club;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.member.Member;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.club.ClubRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.service.BOOKJEOK.dto.club.ClubRequestDto.*;

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
    @Enumerated(EnumType.STRING)
    private ClubStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "club_id")
    private List<TagEntity> tags = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //ERD에 추가
    @OneToMany(mappedBy = "club")
    private List<Member> memberList = new ArrayList<>();

    //ERD에 추가
    @OneToMany(mappedBy = "club")
    private List<Feed> feedList = new ArrayList<>();

    @CreatedDate //insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //insert, update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Embedded
    private Book book = new Book();

    @Builder
    public Club(Long id, String title, String img_url, String contents, int max_personnel, String description, String tags, User user, LocalDateTime createdAt, LocalDateTime updatedAt,
                String bookTitle, String author, String publisher) {
        this.id = id;
        this.title = title;
        this.img_url = img_url;
        this.contents = contents;
        this.max_personnel = max_personnel;
        this.description = description;
        this.likes = 0;
        this.status = ClubStatus.ACTIVE;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.book.bookTitle = bookTitle;
        this.book.author = author;
        this.book.publisher = publisher;
        this.user = user;

        List<TagEntity> tagEntities = makeTags(tags);
        this.tags = tagEntities;
    }

    public void updateClub(ClubUpdateReqDto req) {
        this.title = req.getTitle();
        this.img_url = req.getImg_url();
        this.contents = req.getContents();
        this.max_personnel = req.getMax_personnel();
        this.description = req.getDescription();
        this.updatedAt = LocalDateTime.now();

        Book book = Book.builder()
                .author(req.getAuthor())
                .bookTitle(req.getBookTitle())
                .publisher(req.getPublisher())
                .build();
        this.book = book;

        List<TagEntity> tagEntities = makeTags(req.getTags());
        this.tags.clear();
        this.tags.addAll(
                tagEntities
        );
    }

    private List<TagEntity> makeTags (String tag){
        List<String> tagList = Arrays.asList(tag.split(","));

        List<TagEntity> tags = new ArrayList<>();

        for (int i = 0; i < tagList.size(); i++) {
            if(tagList.get(i).equals(Tag.SMALL.getValue())) tags.add(new TagEntity(Tag.SMALL));
            if(tagList.get(i).equals(Tag.OFFLINE.getValue())) tags.add(new TagEntity(Tag.OFFLINE));
            if(tagList.get(i).equals(Tag.ONLINE.getValue())) tags.add(new TagEntity(Tag.ONLINE));
            if(tagList.get(i).equals(Tag.CAPITAL.getValue())) tags.add(new TagEntity(Tag.CAPITAL));
            if(tagList.get(i).equals(Tag.LOCAL.getValue())) tags.add(new TagEntity(Tag.LOCAL));
        }

        return tags;
    }

    public void createLike() {
        this.likes++;
    }

    public void deleteLike() {
        this.likes--;
    }
}
