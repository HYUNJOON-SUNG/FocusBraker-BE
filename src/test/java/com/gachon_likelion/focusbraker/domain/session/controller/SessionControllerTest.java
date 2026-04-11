package com.gachon_likelion.focusbraker.domain.session.controller;

import tools.jackson.databind.ObjectMapper;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionAbandonResponseDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionRequestDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionResponseDto;
import com.gachon_likelion.focusbraker.domain.session.service.SessionService;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        // userId: 1, intensityLevel: 6 (invalid)
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
}
