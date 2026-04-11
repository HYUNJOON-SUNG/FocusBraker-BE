package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionResponseDto {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("started_at")
    private LocalDateTime startedAt;

    @JsonProperty("intensity_level")
    private Integer intensityLevel;

    @JsonProperty("hair_enabled")
    private Boolean hairEnabled;

    @JsonProperty("dust_enabled")
    private Boolean dustEnabled;

    @JsonProperty("bug_enabled")
    private Boolean bugEnabled;

    @JsonProperty("fake_noti_enabled")
    private Boolean fakeNotiEnabled;

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
