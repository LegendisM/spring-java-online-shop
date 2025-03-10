package com.demisco.quiz.service;

import com.demisco.quiz.dto.auth.AuthTokenResponseDto;
import com.demisco.quiz.dto.auth.LoginRequestDto;
import com.demisco.quiz.dto.auth.RegisterRequestDto;
import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;
    private final AuthTokenService authTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    /**
     * Login with the usernamd and password (default credentials)
     * @param requestDto
     * @return AuthTokenResponse the jwt access token
     * @throws ResponseException
     */
    public AuthTokenResponseDto login(LoginRequestDto requestDto) throws ResponseException {
        var user = userService.findByUsername(requestDto.username);

        if (user == null) {
            throw new ResponseException("invalid-username");
        }

        if (!bCryptPasswordEncoder.matches(requestDto.password, user.getPassword())) {
            throw new ResponseException("invalid-password");
        }

        var token = authTokenService.createToken(user);
        return AuthTokenResponseDto.builder()
                .accessKey(token)
                .build();
    }

    /**
     * Register user with username and password (aefault credentials)
     * @param requestDto
     * @return boolean the state of the registration (default true)
     * @throws ResponseException
     */
    public boolean register(RegisterRequestDto requestDto) throws ResponseException {
        var user = userService.findByUsername(requestDto.username);

        if (user != null) {
            throw new ResponseException("username-already-used");
        }

        user = userService.createUser(requestDto);
        return true;
    }

    public UserEntity validateToken(String token) throws ResponseException {
        var user = authTokenService.validateToken(token);
        logger.info("User with username: {} has been validated", user.getUsername());
        return user;
    }

}