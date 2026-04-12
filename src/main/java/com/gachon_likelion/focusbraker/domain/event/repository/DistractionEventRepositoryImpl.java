package com.gachon_likelion.focusbraker.domain.event.repository;

import com.gachon_likelion.focusbraker.domain.event.entity.DistractionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DistractionEventRepositoryImpl implements DistractionEventRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void saveAllInBatch(List<DistractionEvent> events) {
        String sql = "INSERT INTO distraction_events (session_id, distraction_type, appeared_at, reacted_at, reaction_time_ms, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                events,
                100,
                (ps, event) -> {
                    ps.setLong(1, event.getSession().getId());
                    ps.setString(2, event.getDistractionType().name());
                    ps.setTimestamp(3, Timestamp.valueOf(event.getAppearedAt()));
                    ps.setTimestamp(4, event.getReactedAt() != null ? Timestamp.valueOf(event.getReactedAt()) : null);
                    if (event.getReactionTimeMs() != null) {
                        ps.setInt(5, event.getReactionTimeMs());
                    } else {
                        ps.setNull(5, java.sql.Types.INTEGER);
                    }
                    ps.setTimestamp(6, Timestamp.valueOf(event.getCreatedAt()));
                    ps.setTimestamp(7, Timestamp.valueOf(event.getUpdatedAt()));
                });
    }
}
