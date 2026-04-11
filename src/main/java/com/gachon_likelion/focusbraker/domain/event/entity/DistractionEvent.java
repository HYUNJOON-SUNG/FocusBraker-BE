package com.gachon_likelion.focusbraker.domain.event.entity;

import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.common.BaseEntity;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "distraction_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DistractionEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private OverlaySession session;

    @Enumerated(EnumType.STRING)
    @Column(name = "distraction_type", nullable = false)
    private DistractionType distractionType;

    @Column(name = "appeared_at", nullable = false, columnDefinition = "DATETIME(3)")
    private LocalDateTime appearedAt;

    @Column(name = "reacted_at", columnDefinition = "DATETIME(3)")
    private LocalDateTime reactedAt;

    @Column(name = "reaction_time_ms")
    private Integer reactionTimeMs;

    @Builder
    public DistractionEvent(OverlaySession session, DistractionType distractionType, LocalDateTime appearedAt, LocalDateTime reactedAt, Integer reactionTimeMs) {
        this.session = session;
        this.distractionType = distractionType;
        this.appearedAt = appearedAt;
        this.reactedAt = reactedAt;
        this.reactionTimeMs = reactionTimeMs;
    }
}
