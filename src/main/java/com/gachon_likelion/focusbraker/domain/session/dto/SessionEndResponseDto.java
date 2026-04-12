package com.gachon_likelion.focusbraker.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionEndResponseDto {

    @Schema(description = "종료된 세션 정보")
    private EndedSessionInfoDto session;

    @Schema(description = "세션 리포트")
    private SessionReportDto report;
}
