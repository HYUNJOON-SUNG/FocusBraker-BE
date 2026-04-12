package com.gachon_likelion.focusbraker.domain.session.repository;

import com.gachon_likelion.focusbraker.domain.session.entity.OverlaySession;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OverlaySessionRepository extends JpaRepository<OverlaySession, Long> {
    Optional<OverlaySession> findByUserIdAndStatus(Long userId, SessionStatus status);
}