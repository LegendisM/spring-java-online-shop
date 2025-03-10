package com.demisco.quiz.interceptor;

import com.demisco.quiz.annotation.Auth;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (((HandlerMethod) handler).getBean().getClass().isAnnotationPresent(Auth.class) || ((HandlerMethod) handler).hasMethodAnnotation(Auth.class)) {
                try {
                    var token = request.getHeader("Authorization").split("Bearer ")[1].trim();
                    var user = authService.validateToken(token);
                    request.setAttribute("user", user);
                } catch (Exception exception) {
                    throw new ResponseException("invalid-authentication-token");
                }
            }
        }
        return true;
    }

}
