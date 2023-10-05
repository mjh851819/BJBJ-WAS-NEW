package com.service.BOOKJEOK.controller;

import com.service.BOOKJEOK.dto.ResponseDto;
import com.service.BOOKJEOK.dto.comment.CommentRequestDto;
import com.service.BOOKJEOK.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.service.BOOKJEOK.dto.comment.CommentRequestDto.*;

/*
- 덧글 등록
- 덧글 수정
- 덧글 삭제
- 내가 쓴 덧글 리스트 조회 (최신순)
 */
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentCreateReqDto req, BindingResult bindingResult) {

        commentService.createComment(req);

        return new ResponseEntity<>(new ResponseDto<>(1, "덧글 작성 성공", null), HttpStatus.CREATED);
    }
}
