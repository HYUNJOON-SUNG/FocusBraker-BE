package com.gachon_likelion.focusbraker.domain.user.controller;

import tools.jackson.databind.ObjectMapper;
import com.gachon_likelion.focusbraker.domain.user.dto.UserRequestDto;
import com.gachon_likelion.focusbraker.domain.user.dto.UserResponseDto;
import com.gachon_likelion.focusbraker.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("유저 등록/조회 성공 - 신규 유저")
    void registerOrLogin_Success_NewUser() throws Exception {
        // given
        String deviceUuid = "550e8400-e29b-41d4-a716-446655440000";
        UserRequestDto requestDto = new UserRequestDto(deviceUuid);
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(1L)
                .deviceUuid(deviceUuid)
                .isNew(true)
                .createdAt(LocalDateTime.now())
                .build();

        given(userService.registerOrLogin(any(UserRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.device_uuid").value(deviceUuid))
                .andExpect(jsonPath("$.data.is_new").value(true));
    }

    @Test
    @DisplayName("유저 등록 실패 - 유효하지 않은 UUID 형식")
    void registerOrLogin_Fail_InvalidUuid() throws Exception {
        // given
        String invalidUuid = "invalid-uuid";
        UserRequestDto requestDto = new UserRequestDto(invalidUuid);

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
