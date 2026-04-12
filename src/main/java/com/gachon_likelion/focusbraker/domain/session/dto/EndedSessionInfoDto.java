package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EndedSessionInfoDto {

    @Schema(description = "세션 대리키", example = "1")
    private Long id;

    @Schema(description = "세션 상태", example = "COMPLETED")
    private SessionStatus status;

    @JsonProperty("started_at")
    @Schema(description = "세션 시작 시각", example = "2024-01-15T14:30:00")
    private LocalDateTime startedAt;

    @JsonProperty("ended_at")
    @Schema(description = "세션 종료 시각", example = "2024-01-15T15:00:00")
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
