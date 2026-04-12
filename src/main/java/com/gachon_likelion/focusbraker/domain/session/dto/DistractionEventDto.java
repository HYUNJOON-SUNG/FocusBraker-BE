package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DistractionEventDto {

    @NotNull
    @JsonProperty("distraction_type")
    @Schema(description = "방해 종류", example = "HAIR", requiredMode = Schema.RequiredMode.REQUIRED)
    private DistractionType distractionType;

    @NotNull
    @JsonProperty("appeared_at")
    @Schema(description = "방해 등장 시각 (밀리초 정밀도)", example = "2024-01-15T14:30:15.123", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime appearedAt;

    @JsonProperty("reacted_at")
    @Schema(description = "반응 시각 (밀리초 정밀도, 반응 안 했으면 null)", example = "2024-01-15T14:30:16.456")
    private LocalDateTime reactedAt;

    @Positive(message = "reaction_time_ms는 양수여야 합니다.")
    @JsonProperty("reaction_time_ms")
    @Schema(description = "반응 시간 (ms 단위, 반응 안 했으면 null)", example = "1333")
    private Integer reactionTimeMs;
}
