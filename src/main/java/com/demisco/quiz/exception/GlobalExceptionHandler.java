package com.demisco.quiz.exception;

import com.demisco.quiz.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ApiResponse<String> resolveResponseStatusException(
            ResponseStatusException exception,
            WebRequest request) {
        return new ApiResponse<>(
                false,
                exception.getMessage(),
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<String> resolveMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            WebRequest request) {
        return new ApiResponse<>(
                false,
                String.format("%s should be a valid %s and %s isn't",
                        exception.getName(),
                        exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "field",
                        exception.getValue()),
                "Validation Error"
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> resolveDefaultException(
            Exception exception,
            WebRequest request) {
        return new ApiResponse<>(
                false,
                exception.getMessage() != null ? exception.getMessage() : "Internal Server Error", // TODO: show exception.message just in develop
                "Internal Server Error"
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<String> resolveEntityNotFoundException(
            EntityNotFoundException exception,
            WebRequest request) {
        return new ApiResponse<>(
                false,
                "Unable to find your target domain with this id",
                "Invalid id"
        );
    }
}