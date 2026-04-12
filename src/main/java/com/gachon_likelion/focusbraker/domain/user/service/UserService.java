package com.gachon_likelion.focusbraker.domain.user.service;

import com.gachon_likelion.focusbraker.domain.user.dto.UserRequestDto;
import com.gachon_likelion.focusbraker.domain.user.dto.UserResponseDto;
import com.gachon_likelion.focusbraker.domain.user.entity.User;
import com.gachon_likelion.focusbraker.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto registerOrLogin(UserRequestDto userRequestDto) {
        return userRepository.findByDeviceUuid(userRequestDto.getDeviceUuid())
                .map(user -> UserResponseDto.from(user, false))
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .deviceUuid(userRequestDto.getDeviceUuid())
                            .build();
                    User savedUser = userRepository.save(newUser);
                    return UserResponseDto.from(savedUser, true);
                });
    }
}
