package com.gachon_likelion.focusbraker.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String deviceUuid;

    public UserRequestDto(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }
}
