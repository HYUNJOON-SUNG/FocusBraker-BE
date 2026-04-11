package com.gachon_likelion.focusbraker.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gachon_likelion.focusbraker.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private Long id;

    @JsonProperty("device_uuid")
    private String deviceUuid;

    @JsonProperty("is_new")
    private boolean isNew;

    @JsonProperty("created_at")
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
