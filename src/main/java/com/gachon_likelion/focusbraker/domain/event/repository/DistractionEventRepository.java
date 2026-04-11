package com.gachon_likelion.focusbraker.domain.event.repository;

import com.gachon_likelion.focusbraker.domain.event.entity.DistractionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistractionEventRepository extends JpaRepository<DistractionEvent, Long>, DistractionEventRepositoryCustom {
}
