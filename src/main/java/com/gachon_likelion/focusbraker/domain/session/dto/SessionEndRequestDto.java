package com.gachon_likelion.focusbraker.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionEndRequestDto {

    @NotNull
    @Size(max = 1000, message = "events 배열 최대 크기를 초과했습니다.")
    @Valid
    @Schema(description = "세션 중 발생한 방해 이벤트 목록 (빈 배열 가능, 최대 1000개)", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DistractionEventDto> events;
}
