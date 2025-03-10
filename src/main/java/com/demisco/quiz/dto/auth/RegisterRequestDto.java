package com.demisco.quiz.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDto {

    public String username;
    public String email;
    public String password;

}
