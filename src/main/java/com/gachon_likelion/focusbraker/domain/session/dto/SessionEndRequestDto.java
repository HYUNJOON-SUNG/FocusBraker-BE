package com.gachon_likelion.focusbraker.domain.session.dto;

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
    private List<DistractionEventDto> events;
}
