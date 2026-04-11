package com.gachon_likelion.focusbraker.domain.event.repository;

import com.gachon_likelion.focusbraker.domain.event.entity.DistractionEvent;

import java.util.List;

public interface DistractionEventRepositoryCustom {
    void saveAllInBatch(List<DistractionEvent> events);
}
