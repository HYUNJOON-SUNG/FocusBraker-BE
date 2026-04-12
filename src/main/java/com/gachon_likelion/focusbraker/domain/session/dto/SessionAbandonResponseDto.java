package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionAbandonResponseDto {
    private Long id;
    private SessionStatus status;

    @JsonProperty("ended_at")
    private LocalDateTime endedAt;

    @Builder
    public SessionAbandonResponseDto(Long id, SessionStatus status, LocalDateTime endedAt) {
        this.id = id;
        this.status = status;
        this.endedAt = endedAt;
    }

    public static SessionAbandonResponseDto from(OverlaySession session) {
        return SessionAbandonResponseDto.builder()
                .id(session.getId())
                .status(session.getStatus())
                .endedAt(session.getEndedAt())
                .build();
    }
}
