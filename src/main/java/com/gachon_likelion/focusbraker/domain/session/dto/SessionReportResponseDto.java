package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.session.entity.SessionReport;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessionReportResponseDto {

    private Long id;

    @JsonProperty("session_id")
    private Long sessionId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("total_duration_seconds")
    private Integer totalDurationSeconds;

    @JsonProperty("total_reaction_count")
    private Integer totalReactionCount;

    @JsonProperty("hair_reaction_count")
    private Integer hairReactionCount;

    @JsonProperty("dust_reaction_count")
    private Integer dustReactionCount;

    @JsonProperty("bug_reaction_count")
    private Integer bugReactionCount;

    @JsonProperty("fake_noti_reaction_count")
    private Integer fakeNotiReactionCount;

    @JsonProperty("avg_reaction_time_ms")
    private Integer avgReactionTimeMs;

    @JsonProperty("most_reacted_type")
    private DistractionType mostReactedType;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Builder
    public SessionReportResponseDto(Long id, Long sessionId, Long userId, Integer totalDurationSeconds,
                                    Integer totalReactionCount, Integer hairReactionCount, Integer dustReactionCount,
                                    Integer bugReactionCount, Integer fakeNotiReactionCount,
                                    Integer avgReactionTimeMs, DistractionType mostReactedType, LocalDateTime createdAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.userId = userId;
        this.totalDurationSeconds = totalDurationSeconds;
        this.totalReactionCount = totalReactionCount;
        this.hairReactionCount = hairReactionCount;
        this.dustReactionCount = dustReactionCount;
        this.bugReactionCount = bugReactionCount;
        this.fakeNotiReactionCount = fakeNotiReactionCount;
        this.avgReactionTimeMs = avgReactionTimeMs;
        this.mostReactedType = mostReactedType;
        this.createdAt = createdAt;
    }

    public static SessionReportResponseDto from(SessionReport report) {
        return SessionReportResponseDto.builder()
                .id(report.getId())
                .sessionId(report.getSession().getId())
                .userId(report.getUser().getId())
                .totalDurationSeconds(report.getTotalDurationSeconds())
                .totalReactionCount(report.getTotalReactionCount())
                .hairReactionCount(report.getHairReactionCount())
                .dustReactionCount(report.getDustReactionCount())
                .bugReactionCount(report.getBugReactionCount())
                .fakeNotiReactionCount(report.getFakeNotiReactionCount())
                .avgReactionTimeMs(report.getAvgReactionTimeMs())
                .mostReactedType(report.getMostReactedType())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
