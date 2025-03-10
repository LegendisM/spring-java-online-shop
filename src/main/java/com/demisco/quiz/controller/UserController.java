package com.demisco.quiz.controller;

import com.demisco.quiz.annotation.Auth;
import com.demisco.quiz.annotation.CurrentUser;
import com.demisco.quiz.dto.ApiResponse;
import com.demisco.quiz.dto.user.UserDto;
import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Auth
    public ApiResponse<UserDto> getUser(@CurrentUser UserEntity user) throws ResponseException {
        var userDto = userService.getUserById(user.getId());
        return new ApiResponse<>(true, "successfully", userDto);
    }

}
