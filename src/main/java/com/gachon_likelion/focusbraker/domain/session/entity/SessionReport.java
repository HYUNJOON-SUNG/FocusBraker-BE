package com.gachon_likelion.focusbraker.domain.session.entity;

import com.gachon_likelion.focusbraker.domain.user.entity.User;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SessionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private OverlaySession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_duration_seconds", nullable = false)
    private Integer totalDurationSeconds;

    @Column(name = "total_reaction_count", nullable = false)
    private Integer totalReactionCount;

    @Column(name = "hair_reaction_count", nullable = false)
    private Integer hairReactionCount = 0;

    @Column(name = "dust_reaction_count", nullable = false)
    private Integer dustReactionCount = 0;

    @Column(name = "bug_reaction_count", nullable = false)
    private Integer bugReactionCount = 0;

    @Column(name = "fake_noti_reaction_count", nullable = false)
    private Integer fakeNotiReactionCount = 0;

    @Column(name = "avg_reaction_time_ms")
    private Integer avgReactionTimeMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "most_reacted_type")
    private DistractionType mostReactedType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public SessionReport(OverlaySession session, User user, Integer totalDurationSeconds,
                         Integer totalReactionCount, Integer hairReactionCount, Integer dustReactionCount,
                         Integer bugReactionCount, Integer fakeNotiReactionCount,
                         Integer avgReactionTimeMs, DistractionType mostReactedType) {
        this.session = session;
        this.user = user;
        this.totalDurationSeconds = totalDurationSeconds;
        this.totalReactionCount = totalReactionCount;
        this.hairReactionCount = hairReactionCount != null ? hairReactionCount : 0;
        this.dustReactionCount = dustReactionCount != null ? dustReactionCount : 0;
        this.bugReactionCount = bugReactionCount != null ? bugReactionCount : 0;
        this.fakeNotiReactionCount = fakeNotiReactionCount != null ? fakeNotiReactionCount : 0;
        this.avgReactionTimeMs = avgReactionTimeMs;
        this.mostReactedType = mostReactedType;
    }
}