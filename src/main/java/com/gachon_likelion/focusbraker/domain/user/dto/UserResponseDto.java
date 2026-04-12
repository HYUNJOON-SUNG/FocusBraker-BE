package com.gachon_likelion.focusbraker.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    @Schema(description = "유저 대리키", example = "1")
    private Long id;

    @JsonProperty("device_uuid")
    @Schema(description = "기기 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    private String deviceUuid;

    @JsonProperty("is_new")
    @Schema(description = "신규 유저 여부 (true: 최초 등록, false: 기존 유저)", example = "true")
    private boolean isNew;

    @JsonProperty("created_at")
    @Schema(description = "생성 일시", example = "2024-01-15T10:00:00")
    private LocalDateTime createdAt;

    @Builder
    public UserResponseDto(Long id, String deviceUuid, boolean isNew, LocalDateTime createdAt) {
        this.id = id;
        this.deviceUuid = deviceUuid;
        this.isNew = isNew;
        this.createdAt = createdAt;
    }

    public static UserResponseDto from(User user, boolean isNew) {
        return UserResponseDto.builder()
                .id(user.getId())
                .deviceUuid(user.getDeviceUuid())
                .isNew(isNew)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
