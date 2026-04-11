package com.gachon_likelion.focusbraker.domain.session.repository;

import com.gachon_likelion.focusbraker.domain.session.entity.SessionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionReportRepository extends JpaRepository<SessionReport, Long> {
    Optional<SessionReport> findBySessionId(Long sessionId);
}