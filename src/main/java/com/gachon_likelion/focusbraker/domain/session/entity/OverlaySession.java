package com.gachon_likelion.focusbraker.domain.session.entity;

import com.gachon_likelion.focusbraker.domain.user.entity.User;
import com.gachon_likelion.focusbraker.global.common.BaseEntity;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "overlay_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OverlaySession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "intensity_level", nullable = false)
    private Integer intensityLevel;

    @Column(name = "hair_enabled", nullable = false)
    private Boolean hairEnabled;

    @Column(name = "dust_enabled", nullable = false)
    private Boolean dustEnabled;

    @Column(name = "bug_enabled", nullable = false)
    private Boolean bugEnabled;

    @Column(name = "fake_noti_enabled", nullable = false)
    private Boolean fakeNotiEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.IN_PROGRESS;

    @Builder
    public OverlaySession(User user, LocalDateTime startedAt, Integer intensityLevel,
                          Boolean hairEnabled, Boolean dustEnabled, Boolean bugEnabled, Boolean fakeNotiEnabled) {
        this.user = user;
        this.startedAt = startedAt;
        this.intensityLevel = intensityLevel;
        this.hairEnabled = hairEnabled;
        this.dustEnabled = dustEnabled;
        this.bugEnabled = bugEnabled;
        this.fakeNotiEnabled = fakeNotiEnabled;
    }

    public void endSession(LocalDateTime endedAt) {
        this.endedAt = endedAt;
        this.status = SessionStatus.COMPLETED;
    }

    public void abandonSession(LocalDateTime endedAt) {
        this.endedAt = endedAt;
        this.status = SessionStatus.ABANDONED;
    }
}