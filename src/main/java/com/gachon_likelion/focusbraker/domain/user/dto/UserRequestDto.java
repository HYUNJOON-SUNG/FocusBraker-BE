package com.gachon_likelion.focusbraker.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestDto {

    @JsonProperty("device_uuid")
    @NotBlank(message = "device_uuid는 필수입니다")
    @Size(min = 36, max = 36, message = "유효하지 않은 UUID 형식입니다")
    @Schema(description = "클라이언트 생성 기기 고유 UUID (36자)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deviceUuid;

    public UserRequestDto(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }
}
