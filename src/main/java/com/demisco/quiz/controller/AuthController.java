package com.demisco.quiz.controller;

import com.demisco.quiz.annotation.Auth;
import com.demisco.quiz.annotation.CurrentUser;
import com.demisco.quiz.dto.ApiResponse;
import com.demisco.quiz.dto.auth.AuthTokenResponseDto;
import com.demisco.quiz.dto.auth.LoginRequestDto;
import com.demisco.quiz.dto.auth.RegisterRequestDto;
import com.demisco.quiz.dto.user.UserDto;
import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthTokenResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) throws ResponseException {
        var token = authService.login(requestDto);
        return new ApiResponse<>(true, "successfully", token);
    }

    @PostMapping("/register")
    public ApiResponse<Boolean> login(@RequestBody @Valid RegisterRequestDto requestDto) throws ResponseException {
        var registered = authService.register(requestDto);
        return new ApiResponse<>(true, "successfully", registered);
    }

}
