package com.service.BOOKJEOK.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalStatus {
    MASTER("클럽장"), WAITING("대기중"), CONFIRMED("승인됨");

    private String value;
}
