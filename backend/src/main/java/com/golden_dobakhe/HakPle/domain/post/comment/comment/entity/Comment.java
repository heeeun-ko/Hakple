package com.golden_dobakhe.HakPle.domain.post.comment.comment.entity;

import com.golden_dobakhe.HakPle.domain.post.post.entity.Board;

import com.golden_dobakhe.HakPle.domain.user.user.entity.User;
import com.golden_dobakhe.HakPle.global.Status;
import com.golden_dobakhe.HakPle.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class Comment extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    @ToString.Exclude
    private Board board;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Lob // TEXT 타입
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content; // 내용 (TEXT 타입)

    private int likeCount; // 댓글 좋아요 수

    @Enumerated(EnumType.STRING)
    private Status status; // ENUM('active', 'inactive', 'pending')
}
