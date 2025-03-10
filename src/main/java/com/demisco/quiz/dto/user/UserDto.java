package com.demisco.quiz.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String username;
    private String email;
}
