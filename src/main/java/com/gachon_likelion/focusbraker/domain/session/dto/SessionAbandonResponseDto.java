package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionAbandonResponseDto {

    @Schema(description = "세션 대리키", example = "1")
    private Long id;

    @Schema(description = "세션 상태", example = "ABANDONED")
    private SessionStatus status;

    @JsonProperty("ended_at")
    @Schema(description = "세션 종료 시각 (중단 시각)", example = "2024-01-15T15:00:00")
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
