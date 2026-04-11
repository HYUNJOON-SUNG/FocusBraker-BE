package com.gachon_likelion.focusbraker.domain.session.dto;

import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EndedSessionInfoDto {
    private Long id;
    private SessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Builder
    public EndedSessionInfoDto(Long id, SessionStatus status, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.id = id;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static EndedSessionInfoDto from(OverlaySession session) {
        return EndedSessionInfoDto.builder()
                .id(session.getId())
                .status(session.getStatus())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .build();
    }
}
