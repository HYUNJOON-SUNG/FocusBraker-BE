package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
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
    private DistractionType distractionType;

    @NotNull
    @JsonProperty("appeared_at")
    private LocalDateTime appearedAt;

    @JsonProperty("reacted_at")
    private LocalDateTime reactedAt;

    @Positive(message = "reaction_time_ms는 양수여야 합니다.")
    @JsonProperty("reaction_time_ms")
    private Integer reactionTimeMs;
}
