package com.gachon_likelion.focusbraker.domain.session.controller;

import com.gachon_likelion.focusbraker.domain.session.dto.SessionAbandonResponseDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionRequestDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionResponseDto;
import com.gachon_likelion.focusbraker.domain.session.service.SessionService;
import com.gachon_likelion.focusbraker.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SessionResponseDto>> startSession(@RequestBody @Valid SessionRequestDto requestDto) {
        SessionResponseDto response = sessionService.startSession(requestDto);
        return new ResponseEntity<>(ApiResponse.created(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{sessionId}/abandon")
    public ApiResponse<SessionAbandonResponseDto> abandonSession(@PathVariable Long sessionId) {
        SessionAbandonResponseDto response = sessionService.abandonSession(sessionId);
        return ApiResponse.ok(response);
    }
}
