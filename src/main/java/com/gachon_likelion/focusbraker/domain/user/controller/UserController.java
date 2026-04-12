package com.gachon_likelion.focusbraker.domain.user.controller;

import com.gachon_likelion.focusbraker.domain.user.dto.UserRequestDto;
import com.gachon_likelion.focusbraker.domain.user.dto.UserResponseDto;
import com.gachon_likelion.focusbraker.domain.user.service.UserService;
import com.gachon_likelion.focusbraker.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponseDto> registerOrLogin(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto response = userService.registerOrLogin(userRequestDto);
        return ApiResponse.ok(response);
    }
}
