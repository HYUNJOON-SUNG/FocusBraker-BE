package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionResponseDto {

    @Schema(description = "세션 대리키", example = "1")
    private Long id;

    @JsonProperty("user_id")
    @Schema(description = "유저 대리키", example = "1")
    private Long userId;

    @JsonProperty("started_at")
    @Schema(description = "세션 시작 시각", example = "2024-01-15T14:30:00")
    private LocalDateTime startedAt;

    @JsonProperty("intensity_level")
    @Schema(description = "강도 스냅샷", example = "3")
    private Integer intensityLevel;

    @JsonProperty("hair_enabled")
    @Schema(description = "머리카락 활성화 스냅샷", example = "true")
    private Boolean hairEnabled;

    @JsonProperty("dust_enabled")
    @Schema(description = "먼지 활성화 스냅샷", example = "true")
    private Boolean dustEnabled;

    @JsonProperty("bug_enabled")
    @Schema(description = "벌레 활성화 스냅샷", example = "true")
    private Boolean bugEnabled;

    @JsonProperty("fake_noti_enabled")
    @Schema(description = "가짜 알림 활성화 스냅샷", example = "true")
    private Boolean fakeNotiEnabled;

    @Schema(description = "세션 상태", example = "IN_PROGRESS")
    private SessionStatus status;

    @Builder
    public SessionResponseDto(Long id, Long userId, LocalDateTime startedAt, Integer intensityLevel,
                              Boolean hairEnabled, Boolean dustEnabled, Boolean bugEnabled,
                              Boolean fakeNotiEnabled, SessionStatus status) {
        this.id = id;
        this.userId = userId;
        this.startedAt = startedAt;
        this.intensityLevel = intensityLevel;
        this.hairEnabled = hairEnabled;
        this.dustEnabled = dustEnabled;
        this.bugEnabled = bugEnabled;
        this.fakeNotiEnabled = fakeNotiEnabled;
        this.status = status;
    }

    public static SessionResponseDto from(OverlaySession session) {
        return SessionResponseDto.builder()
                .id(session.getId())
                .userId(session.getUser().getId())
                .startedAt(session.getStartedAt())
                .intensityLevel(session.getIntensityLevel())
                .hairEnabled(session.getHairEnabled())
                .dustEnabled(session.getDustEnabled())
                .bugEnabled(session.getBugEnabled())
                .fakeNotiEnabled(session.getFakeNotiEnabled())
                .status(session.getStatus())
                .build();
    }
}
