package com.demisco.quiz.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenResponseDto {

    public String accessKey;

}
