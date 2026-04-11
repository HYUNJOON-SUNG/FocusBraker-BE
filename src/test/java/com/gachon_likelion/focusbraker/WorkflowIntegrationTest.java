package com.gachon_likelion.focusbraker;

import tools.jackson.databind.ObjectMapper;
import com.gachon_likelion.focusbraker.domain.session.dto.DistractionEventDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionEndRequestDto;
import com.gachon_likelion.focusbraker.domain.session.dto.SessionRequestDto;
import com.gachon_likelion.focusbraker.domain.user.dto.UserRequestDto;
import com.gachon_likelion.focusbraker.global.enums.DistractionType;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("S01 ~ S06 전체 워크플로우 통합 테스트")
    void fullWorkflowTest() throws Exception {
        
        // 1. 유저 생성
        String deviceUuid = UUID.randomUUID().toString();
        UserRequestDto userReq = new UserRequestDto(deviceUuid);
        MvcResult userResult = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isOk())
                .andReturn();
        Long userId = ((Number) JsonPath.read(userResult.getResponse().getContentAsString(), "$.data.id")).longValue();

        // 2. 세션 시작
        SessionRequestDto sessionReq = new SessionRequestDto(userId, 3, true, true, true, true);
        MvcResult sessionResult = mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionReq)))
                .andExpect(status().isCreated())
                .andReturn();
        
        // 시간 간격 확보를 위한 대기
        Thread.sleep(1000);

        String sessionRes = sessionResult.getResponse().getContentAsString();
        Long sessionId = ((Number) JsonPath.read(sessionRes, "$.data.id")).longValue();
        
        // started_at < eventTime < ended_at 조건을 만족하도록 설정
        LocalDateTime eventTime = LocalDateTime.now().minusNanos(500L * 1_000_000);

        // 3. 세션 종료
        DistractionEventDto e1 = new DistractionEventDto(DistractionType.HAIR, eventTime, eventTime.plusNanos(100L * 1_000_000), 100);
        SessionEndRequestDto endReq = new SessionEndRequestDto(List.of(e1));

        mockMvc.perform(patch("/api/v1/sessions/{sessionId}/end", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.report.total_reaction_count").value(1));

        // 4. 리포트 조회
        mockMvc.perform(get("/api/v1/sessions/{sessionId}/report", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total_reaction_count").value(1));
    }
}
