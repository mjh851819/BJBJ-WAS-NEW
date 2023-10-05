package com.service.BOOKJEOK.repository.comment;

import com.service.BOOKJEOK.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
