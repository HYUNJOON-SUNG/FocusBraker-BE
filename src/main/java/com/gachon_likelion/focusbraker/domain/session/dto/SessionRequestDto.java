package com.gachon_likelion.focusbraker.domain.session.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long userId;

    @NotNull(message = "intensity_level은 필수입니다.")
    @Min(value = 1, message = "intensity_level은 1~5 사이의 값이어야 합니다.")
    @Max(value = 5, message = "intensity_level은 1~5 사이의 값이어야 합니다.")
    @JsonProperty("intensity_level")
    private Integer intensityLevel;

    @NotNull(message = "hair_enabled은 필수입니다.")
    @JsonProperty("hair_enabled")
    private Boolean hairEnabled;

    @NotNull(message = "dust_enabled은 필수입니다.")
    @JsonProperty("dust_enabled")
    private Boolean dustEnabled;

    @NotNull(message = "bug_enabled은 필수입니다.")
    @JsonProperty("bug_enabled")
    private Boolean bugEnabled;

    @NotNull(message = "fake_noti_enabled은 필수입니다.")
    @JsonProperty("fake_noti_enabled")
    private Boolean fakeNotiEnabled;
}
