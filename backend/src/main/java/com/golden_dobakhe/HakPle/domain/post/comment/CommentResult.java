package com.golden_dobakhe.HakPle.domain.post.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentResult {
    USER_NOT_FOUND("사용자를 찾지 못함"),
    COMMENT_NOT_FOUND("댓글을 찾지 못함"),
    BOARD_NOT_FOUND("게시글을 찾지 못함"),
    UNAUTHORIZED("권한 없음"),
    EMPTY("댓글 내용이 비어 있음"),
    SUCCESS("성공적으로 처리됨"),
    NOT_LIKED_YET("아직 좋아요 누르지 않았음"),
    ALREADY_LIKED("이미 좋아요 누름"),
    ALREADY_REPORT("이미 신고함"),
    TOO_LONG("댓글이 너무 깁니다"),
    CANNOT_REPORT_OWN_COMMENT("자신의 댓글은 신고하지 못합니다");


    @Getter
    private final String description;

    CommentResult(String description) {
        this.description = description;
    }

}