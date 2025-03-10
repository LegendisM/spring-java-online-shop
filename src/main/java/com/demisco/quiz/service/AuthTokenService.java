package com.demisco.quiz.service;

import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.exception.ResponseException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class AuthTokenService {
    private final UserService userService;
    private final Key accessKey;

    public AuthTokenService(UserService userService) {
        this.userService = userService;
        this.accessKey = Keys.hmacShaKeyFor("advanced-access-token-key-more-than-256-bits".getBytes());
    }

    /**
     * Make new access token (jwt) by the user
     * @param payload
     * @return access token as string
     */
    public String createToken(UserEntity payload) {
        Date issuedAt = new Date();
        String token;
        Date expirationAt = new Date(issuedAt.getTime() + 21600000);
        token = Jwts.builder()
                .setSubject(payload.getUsername())
                .addClaims(Map.of(
                        "id", payload.getId(),
                        "username", payload.getUsername()
                ))
                .setIssuedAt(issuedAt)
                .setExpiration(expirationAt)
                .signWith(accessKey)
                .compact();
        return token;
    }

    /**
     * Validate token with extract the data and find the user
     * @param token
     * @return UserEntity founded user with the token details
     * @throws ResponseException
     */
    public UserEntity validateToken(String token) throws ResponseException {
        var parser = Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build();

        @SuppressWarnings("unchecked")
        var body = (Map<String, String>) parser.parse(token).getBody();

        return userService.findById(Long.valueOf(body.get("id")));
    }
}