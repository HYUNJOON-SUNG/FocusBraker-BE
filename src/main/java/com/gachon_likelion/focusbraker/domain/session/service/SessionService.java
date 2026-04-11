package com.gachon_likelion.focusbraker.domain.session.service;

import com.gachon_likelion.focusbraker.domain.session.dto.SessionAbandonResponseDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionRequestDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionResponseDto;
import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.domain.session.repository.OverlaySessionRepository;
import com.gachon_likelion.focusbraker.domain.user.entity.User;
import com.gachon_likelion.focusbraker.domain.user.repository.UserRepository;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import com.gachon_likelion.focusbraker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final OverlaySessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SessionResponseDto startSession(SessionRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(404, "유저를 찾을 수 없습니다."));

        // 이미 진행 중인 세션이 있는지 확인
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
    public SessionAbandonResponseDto abandonSession(Long sessionId) {
        OverlaySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(404, "세션을 찾을 수 없습니다."));

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new CustomException(409, "이미 종료된 세션입니다.");
        }

        session.abandonSession(LocalDateTime.now());
        return SessionAbandonResponseDto.from(session);
    }
}
