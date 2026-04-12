package com.gachon_likelion.focusbraker.domain.session.controller;

import com.gachon_likelion.focusbraker.domain.session.dto.*;
import com.gachon_likelion.focusbraker.domain.session.service.SessionService;
import com.gachon_likelion.focusbraker.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Tag(name = "Session", description = "세션 및 리포트 API")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    @Operation(summary = "세션 시작", description = "[오버레이 시작] 버튼 탭 시 호출. 새로운 OverlaySession을 생성하고, 현재 방해 설정을 스냅샷으로 저장한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "세션 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "강도 범위 오류 또는 필수값 누락"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 user_id"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 진행 중인 세션이 있음")
    })
    public ResponseEntity<ApiResponse<SessionResponseDto>> startSession(@RequestBody @Valid SessionRequestDto requestDto) {
        SessionResponseDto response = sessionService.startSession(requestDto);
        return new ResponseEntity<>(ApiResponse.created(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{sessionId}/end")
    @Operation(summary = "세션 종료", description = "오버레이 종료 버튼 탭 시 호출. 클라이언트에서 로컬로 수집한 방해 이벤트를 일괄 전송하고, 서버에서 DistractionEvent를 저장한 뒤 SessionReport를 계산하여 생성한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "세션 종료 및 리포트 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 distraction_type / reaction_time_ms 양수 위반 / events 최대 크기 초과 등"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 sessionId"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 종료된 세션")
    })
    public ApiResponse<SessionEndResponseDto> endSession(
            @Parameter(description = "종료할 세션 ID") @PathVariable Long sessionId,
            @RequestBody @Valid SessionEndRequestDto requestDto) {
        SessionEndResponseDto response = sessionService.endSession(sessionId, requestDto);
        return ApiResponse.ok(response);
    }

    @PatchMapping("/{sessionId}/abandon")
    @Operation(summary = "세션 중단", description = "앱 강제 종료 등 비정상 종료 시 호출. 세션 상태를 ABANDONED로 변경한다. 이벤트 저장 및 리포트 생성은 하지 않는다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "세션 중단 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 sessionId"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 종료된 세션")
    })
    public ApiResponse<SessionAbandonResponseDto> abandonSession(
            @Parameter(description = "중단할 세션 ID") @PathVariable Long sessionId) {
        SessionAbandonResponseDto response = sessionService.abandonSession(sessionId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/{sessionId}/report")
    @Operation(summary = "세션 리포트 조회", description = "S06 화면 재진입 시 기존 리포트를 다시 조회할 때 사용. 세션 종료 응답에 이미 리포트가 포함되므로, 일반적인 흐름에서는 호출하지 않아도 된다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "리포트 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 sessionId 또는 리포트 미생성")
    })
    public ApiResponse<SessionReportResponseDto> getSessionReport(
            @Parameter(description = "조회할 세션 ID") @PathVariable Long sessionId) {
        SessionReportResponseDto response = sessionService.getSessionReport(sessionId);
        return ApiResponse.ok(response);
    }
}
