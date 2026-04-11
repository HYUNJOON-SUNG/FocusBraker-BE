package com.gachon_likelion.focusbraker.domain.session.service;

import com.gachon_likelion.focusbraker.domain.event.entity.DistractionEvent;
import com.gachon_likelion.focusbraker.domain.event.repository.DistractionEventRepository;
import com.gachon_likelion.focusbraker.domain.session.dto.*;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.domain.session.entity.SessionReport;
import com.gachon_likelion.focusbraker.domain.session.repository.OverlaySessionRepository;
import com.gachon_likelion.focusbraker.domain.session.repository.SessionReportRepository;
import com.gachon_likelion.focusbraker.domain.user.entity.User;
import com.gachon_likelion.focusbraker.domain.user.repository.UserRepository;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import com.gachon_likelion.focusbraker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final OverlaySessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final DistractionEventRepository distractionEventRepository;
    private final SessionReportRepository sessionReportRepository;

    @Transactional
    public SessionResponseDto startSession(SessionRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(404, "유저를 찾을 수 없습니다."));

        sessionRepository.findByUserIdAndStatus(user.getId(), SessionStatus.IN_PROGRESS)
                .ifPresent(s -> {
                    throw new CustomException(409, "이미 진행 중인 세션이 있습니다.");
                });

        OverlaySession session = OverlaySession.builder()
                .user(user)
                .startedAt(LocalDateTime.now())
                .intensityLevel(requestDto.getIntensityLevel())
                .hairEnabled(requestDto.getHairEnabled())
                .dustEnabled(requestDto.getDustEnabled())
                .bugEnabled(requestDto.getBugEnabled())
                .fakeNotiEnabled(requestDto.getFakeNotiEnabled())
                .build();

        OverlaySession savedSession = sessionRepository.save(session);
        return SessionResponseDto.from(savedSession);
    }

    @Transactional
    public SessionEndResponseDto endSession(Long sessionId, SessionEndRequestDto requestDto) {
        final LocalDateTime endedAt = LocalDateTime.now();
        OverlaySession session = findAndValidateSession(sessionId);

        List<DistractionEventDto> eventDtos = requestDto.getEvents();
        validateEvents(eventDtos, session.getStartedAt(), endedAt);

        List<DistractionEvent> events = eventDtos.stream()
                .map(dto -> {
                    DistractionEvent event = DistractionEvent.builder()
                            .session(session)
                            .distractionType(dto.getDistractionType())
                            .appearedAt(dto.getAppearedAt())
                            .reactedAt(dto.getReactedAt())
                            .reactionTimeMs(dto.getReactionTimeMs())
                            .build();
                    // Manually set auditing fields for bulk insert using helper method
                    event.setAuditTime(endedAt);
                    return event;
                })
                .collect(Collectors.toList());

        if (!events.isEmpty()) {
            distractionEventRepository.saveAllInBatch(events);
        }

        session.endSession(endedAt);

        SessionReport report = createAndSaveReport(session, events);

        return new SessionEndResponseDto(EndedSessionInfoDto.from(session), SessionReportDto.from(report));
    }

    @Transactional
    public SessionAbandonResponseDto abandonSession(Long sessionId) {
        OverlaySession session = findAndValidateSession(sessionId);
        session.abandonSession(LocalDateTime.now());
        return SessionAbandonResponseDto.from(session);
    }

    @Transactional(readOnly = true)
    public SessionReportResponseDto getSessionReport(Long sessionId) {
        SessionReport report = sessionReportRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomException(404, "리포트가 아직 생성되지 않았습니다."));

        return SessionReportResponseDto.from(report);
    }

    private OverlaySession findAndValidateSession(Long sessionId) {
        OverlaySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(404, "세션을 찾을 수 없습니다."));

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new CustomException(409, "이미 종료된 세션입니다.");
        }
        return session;
    }

    private void validateEvents(List<DistractionEventDto> events, LocalDateTime startedAt, LocalDateTime endedAt) {
        Set<LocalDateTime> appearedAtSet = new HashSet<>();
        for (DistractionEventDto event : events) {
            if (!appearedAtSet.add(event.getAppearedAt())) {
                throw new CustomException(400, "events 배열에 중복 appeared_at이 존재합니다.");
            }
            if (event.getAppearedAt().isBefore(startedAt) || event.getAppearedAt().isAfter(endedAt)) {
                throw new CustomException(400, "appeared_at이 세션 시간 범위를 벗어납니다.");
            }
            boolean hasReactedAt = event.getReactedAt() != null;
            boolean hasReactionTime = event.getReactionTimeMs() != null;
            if (hasReactedAt != hasReactionTime) {
                throw new CustomException(400, "reacted_at과 reaction_time_ms는 함께 전달되거나 함께 null이어야 합니다.");
            }
        }
    }
    private SessionReport createAndSaveReport(OverlaySession session, List<DistractionEvent> events) {
        long totalDurationSeconds = Duration.between(session.getStartedAt(), session.getEndedAt()).getSeconds();
        Map<DistractionType, Integer> reactionCounts = calculateReactionCounts(events);
        int totalReactionCount = reactionCounts.values().stream().mapToInt(Integer::intValue).sum();
        Integer avgReactionTimeMs = calculateAverageReactionTime(events);
        DistractionType mostReactedType = findMostReactedType(reactionCounts);

        SessionReport report = SessionReport.builder()
                .session(session)
                .user(session.getUser())
                .totalDurationSeconds((int) totalDurationSeconds)
                .totalReactionCount(totalReactionCount)
                .hairReactionCount(reactionCounts.getOrDefault(DistractionType.HAIR, 0))
                .dustReactionCount(reactionCounts.getOrDefault(DistractionType.DUST, 0))
                .bugReactionCount(reactionCounts.getOrDefault(DistractionType.BUG, 0))
                .fakeNotiReactionCount(reactionCounts.getOrDefault(DistractionType.FAKE_NOTIFICATION, 0))
                .avgReactionTimeMs(avgReactionTimeMs)
                .mostReactedType(mostReactedType)
                .build();

        return sessionReportRepository.save(report);
    }

    private Map<DistractionType, Integer> calculateReactionCounts(List<DistractionEvent> events) {
        return events.stream()
                .filter(e -> e.getReactedAt() != null)
                .collect(Collectors.groupingBy(
                        DistractionEvent::getDistractionType,
                        Collectors.summingInt(e -> 1)
                ));
    }

    private Integer calculateAverageReactionTime(List<DistractionEvent> events) {
        Double average = events.stream()
                .filter(e -> e.getReactionTimeMs() != null)
                .mapToInt(DistractionEvent::getReactionTimeMs)
                .average()
                .orElse(-1.0);

        return average == -1.0 ? null : average.intValue();
    }

    private DistractionType findMostReactedType(Map<DistractionType, Integer> reactionCounts) {
        if (reactionCounts.isEmpty()) {
            return null;
        }

        Map.Entry<DistractionType, Integer> maxEntry = null;
        for (Map.Entry<DistractionType, Integer> entry : reactionCounts.entrySet()) {
            if (maxEntry == null) {
                maxEntry = entry;
                continue;
            }
            if (entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            } else if (entry.getValue().equals(maxEntry.getValue())) {
                if (entry.getKey().ordinal() < maxEntry.getKey().ordinal()) {
                    maxEntry = entry;
                }
            }
        }
        return maxEntry.getKey();
    }
}
