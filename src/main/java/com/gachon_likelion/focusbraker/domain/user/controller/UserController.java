package com.gachon_likelion.focusbraker.domain.user.controller;

import com.gachon_likelion.focusbraker.domain.user.dto.UserRequestDto;
import com.gachon_likelion.focusbraker.domain.user.dto.UserResponseDto;
import com.gachon_likelion.focusbraker.domain.user.service.UserService;
import com.gachon_likelion.focusbraker.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 등록 및 조회 API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "유저 등록 / 조회", description = "앱 최초 실행 시 또는 재실행 시 호출. 전달받은 device_uuid로 기존 유저를 조회하고, 없으면 새로 생성한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 조회 또는 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "device_uuid 누락 또는 UUID 형식 오류")
    })
    public ApiResponse<UserResponseDto> registerOrLogin(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto response = userService.registerOrLogin(userRequestDto);
        return ApiResponse.ok(response);
    }
}
