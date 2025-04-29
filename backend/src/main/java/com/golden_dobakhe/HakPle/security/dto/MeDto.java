package com.golden_dobakhe.HakPle.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MeDto {
    private final Long id;
    private final String nickname;
    private final LocalDateTime creationTime;
    private final LocalDateTime modificationTime;
    private final String academyId;

    public MeDto(Long id, String nickname, LocalDateTime creationTime, LocalDateTime modificationTime) {
        this.id = id;
        this.nickname = nickname;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.academyId = null;
    }

    public MeDto(Long id, String nickname, LocalDateTime creationTime, LocalDateTime modificationTime, String academyId) {
        this.id = id;
        this.nickname = nickname;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.academyId = academyId;
    }
}
