package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SessionRequestDto {

    @NotNull(message = "user_id는 필수입니다.")
    @JsonProperty("user_id")
    @Schema(description = "유저 대리키", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "intensity_level은 필수입니다.")
    @Min(value = 1, message = "intensity_level은 1~5 사이의 값이어야 합니다.")
    @Max(value = 5, message = "intensity_level은 1~5 사이의 값이어야 합니다.")
    @JsonProperty("intensity_level")
    @Schema(description = "방해 강도 (1~5)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer intensityLevel;

    @NotNull(message = "hair_enabled은 필수입니다.")
    @JsonProperty("hair_enabled")
    @Schema(description = "머리카락 활성화 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean hairEnabled;

    @NotNull(message = "dust_enabled은 필수입니다.")
    @JsonProperty("dust_enabled")
    @Schema(description = "먼지 활성화 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean dustEnabled;

    @NotNull(message = "bug_enabled은 필수입니다.")
    @JsonProperty("bug_enabled")
    @Schema(description = "벌레 활성화 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean bugEnabled;

    @NotNull(message = "fake_noti_enabled은 필수입니다.")
    @JsonProperty("fake_noti_enabled")
    @Schema(description = "가짜 알림 활성화 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean fakeNotiEnabled;
}
