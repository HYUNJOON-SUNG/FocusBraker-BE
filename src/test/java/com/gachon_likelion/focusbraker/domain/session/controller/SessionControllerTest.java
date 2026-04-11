package com.gachon_likelion.focusbraker.domain.session.controller;

import tools.jackson.databind.ObjectMapper;
import com.gachon_likelion.focusbraker.domain.session.dto.*;
import com.gachon_likelion.focusbraker.domain.session.service.SessionService;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import com.gachon_likelion.focusbraker.global.enums.SessionStatus;
import com.gachon_likelion.focusbraker.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @Test
    @DisplayName("세션 시작 성공 - 201 Created")
    void startSession_Success() throws Exception {
        // given
        SessionRequestDto requestDto = new SessionRequestDto(1L, 3, true, true, true, true);
        SessionResponseDto responseDto = SessionResponseDto.builder()
                .id(1L)
                .userId(1L)
                .startedAt(LocalDateTime.now())
                .intensityLevel(3)
                .hairEnabled(true)
                .dustEnabled(true)
                .bugEnabled(true)
                .fakeNotiEnabled(true)
                .status(SessionStatus.IN_PROGRESS)
                .build();

        given(sessionService.startSession(any(SessionRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("CREATED"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.data.intensity_level").value(3));
    }

    @Test
    @DisplayName("세션 시작 실패 - 존재하지 않는 유저 - 404 Not Found")
    void startSession_Fail_UserNotFound() throws Exception {
        // given
        SessionRequestDto requestDto = new SessionRequestDto(999L, 3, true, true, true, true);
        given(sessionService.startSession(any(SessionRequestDto.class)))
                .willThrow(new CustomException(404, "유저를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("세션 시작 실패 - 이미 진행중인 세션 존재 - 409 Conflict")
    void startSession_Fail_SessionInProgress() throws Exception {
        // given
        SessionRequestDto requestDto = new SessionRequestDto(1L, 3, true, true, true, true);
        given(sessionService.startSession(any(SessionRequestDto.class)))
                .willThrow(new CustomException(409, "이미 진행 중인 세션이 있습니다."));

        // when & then
        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("이미 진행 중인 세션이 있습니다."));
    }


    @Test
    @DisplayName("세션 시작 실패 - intensity_level 범위 오류 (1~5) - 400 Bad Request")
    void startSession_Fail_InvalidIntensity() throws Exception {
        // given
        String requestBody = "{\"user_id\":1,\"intensity_level\":6,\"hair_enabled\":true,\"dust_enabled\":true,\"bug_enabled\":true,\"fake_noti_enabled\":true}";

        // when & then
        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("세션 중단 성공 - 200 OK")
    void abandonSession_Success() throws Exception {
        // given
        Long sessionId = 1L;
        SessionAbandonResponseDto responseDto = SessionAbandonResponseDto.builder()
                .id(sessionId)
                .status(SessionStatus.ABANDONED)
                .endedAt(LocalDateTime.now())
                .build();

        given(sessionService.abandonSession(sessionId)).willReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/abandon", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.status").value("ABANDONED"));
    }

    @Test
    @DisplayName("세션 중단 실패 - 존재하지 않는 세션 - 404 Not Found")
    void abandonSession_Fail_SessionNotFound() throws Exception {
        // given
        Long sessionId = 999L;
        given(sessionService.abandonSession(sessionId))
                .willThrow(new CustomException(404, "세션을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/abandon", sessionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("세션을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("세션 중단 실패 - 이미 종료된 세션 - 409 Conflict")
    void abandonSession_Fail_AlreadyEnded() throws Exception {
        // given
        Long sessionId = 1L;
        given(sessionService.abandonSession(sessionId))
                .willThrow(new CustomException(409, "이미 종료된 세션입니다."));

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/abandon", sessionId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("이미 종료된 세션입니다."));
    }

    @Test
    @DisplayName("세션 종료 성공 - 200 OK")
    void endSession_Success() throws Exception {
        // given
        Long sessionId = 1L;
        DistractionEventDto event1 = new DistractionEventDto(DistractionType.HAIR, LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(9), 60000);
        SessionEndRequestDto requestDto = new SessionEndRequestDto(List.of(event1));

        SessionEndResponseDto responseDto = new SessionEndResponseDto(
                EndedSessionInfoDto.builder().id(sessionId).status(SessionStatus.COMPLETED).build(),
                SessionReportDto.builder().totalReactionCount(1).mostReactedType(DistractionType.HAIR).build()
        );

        given(sessionService.endSession(eq(sessionId), any(SessionEndRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/end", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.session.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.report.total_reaction_count").value(1));
    }

    @Test
    @DisplayName("세션 종료 실패 - 이미 종료된 세션 - 409 Conflict")
    void endSession_Fail_AlreadyEnded() throws Exception {
        // given
        Long sessionId = 1L;
        SessionEndRequestDto requestDto = new SessionEndRequestDto(Collections.emptyList());

        given(sessionService.endSession(eq(sessionId), any(SessionEndRequestDto.class)))
                .willThrow(new CustomException(409, "이미 종료된 세션입니다."));

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/end", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("이미 종료된 세션입니다."));
    }

    @Test
    @DisplayName("세션 종료 실패 - 이벤트 리스트 유효성 검증 실패 - 400 Bad Request")
    void endSession_Fail_InvalidEvents() throws Exception {
        // given
        Long sessionId = 1L;
        // reacted_at is null but reaction_time_ms is not
        DistractionEventDto invalidEvent = new DistractionEventDto(DistractionType.BUG, LocalDateTime.now(), null, 1000);
        SessionEndRequestDto requestDto = new SessionEndRequestDto(List.of(invalidEvent));

        given(sessionService.endSession(eq(sessionId), any(SessionEndRequestDto.class)))
                .willThrow(new CustomException(400, "reacted_at과 reaction_time_ms는 함께 전달되거나 함께 null이어야 합니다."));

        // when & then
        ResultActions result = mockMvc.perform(patch("/api/v1/sessions/{sessionId}/end", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("reacted_at과 reaction_time_ms는 함께 전달되거나 함께 null이어야 합니다."));
    }

    @Test
    @DisplayName("세션 종료 성공 - 응답 필드명(snake_case) 및 평균 시간 null 확인")
    void endSession_Success_CheckFieldsAndNull() throws Exception {
        // given
        Long sessionId = 1L;
        SessionEndRequestDto requestDto = new SessionEndRequestDto(Collections.emptyList());

        SessionEndResponseDto responseDto = new SessionEndResponseDto(
                EndedSessionInfoDto.builder()
                        .id(sessionId)
                        .status(SessionStatus.COMPLETED)
                        .startedAt(LocalDateTime.now().minusHours(1))
                        .endedAt(LocalDateTime.now())
                        .build(),
                SessionReportDto.builder()
                        .totalReactionCount(0)
                        .avgReactionTimeMs(null)
                        .build()
        );

        given(sessionService.endSession(eq(sessionId), any(SessionEndRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/end", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.session.started_at").exists())
                .andExpect(jsonPath("$.data.session.ended_at").exists())
                .andExpect(jsonPath("$.data.report.avg_reaction_time_ms").value(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    @DisplayName("세션 종료 실패 - 유효하지 않은 distraction_type - 400 Bad Request")
    void endSession_Fail_InvalidEnum() throws Exception {
        // given
        Long sessionId = 1L;
        String invalidJson = "{\"events\": [{\"distraction_type\": \"INVALID_TYPE\", \"appeared_at\": \"2026-04-12T02:00:00.000\"}]}";

        // when & then
        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/end", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("유효하지 않은 distraction_type입니다"));
    }

    @Test
    @DisplayName("리포트 조회 성공 - 200 OK")
    void getSessionReport_Success() throws Exception {
        // given
        Long sessionId = 1L;
        SessionReportResponseDto responseDto = SessionReportResponseDto.builder()
                .id(1L)
                .sessionId(sessionId)
                .userId(1L)
                .totalDurationSeconds(1800)
                .totalReactionCount(5)
                .hairReactionCount(2)
                .dustReactionCount(1)
                .bugReactionCount(1)
                .fakeNotiReactionCount(1)
                .avgReactionTimeMs(1200)
                .mostReactedType(DistractionType.HAIR)
                .createdAt(LocalDateTime.now())
                .build();

        given(sessionService.getSessionReport(sessionId)).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/v1/sessions/{sessionId}/report", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.session_id").value(sessionId))
                .andExpect(jsonPath("$.data.total_reaction_count").value(5));
    }

    @Test
    @DisplayName("리포트 조회 실패 - 리포트 없음 - 404 Not Found")
    void getSessionReport_Fail_NotFound() throws Exception {
        // given
        Long sessionId = 999L;
        given(sessionService.getSessionReport(sessionId))
                .willThrow(new CustomException(404, "리포트가 아직 생성되지 않았습니다."));

        // when & then
        mockMvc.perform(get("/api/v1/sessions/{sessionId}/report", sessionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("리포트가 아직 생성되지 않았습니다."));
    }
}
