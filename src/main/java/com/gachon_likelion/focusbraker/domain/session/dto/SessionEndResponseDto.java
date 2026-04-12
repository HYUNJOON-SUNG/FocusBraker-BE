package com.gachon_likelion.focusbraker.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionEndResponseDto {
    private EndedSessionInfoDto session;
    private SessionReportDto report;
}
