package com.epam.songservice.exception;

import com.epam.songservice.dto.ErrorDto;
import com.epam.songservice.dto.ValidationErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Validation Error: {}. handleValidationExceptions: errors={}", ex.getMessage(), errors);
        return new ValidationErrorDto("Validation error", errors, "400");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Bad Request: {}. handleIllegalArgumentException: message={}", ex.getMessage(), ex.getMessage());
        return new ErrorDto(ex.getMessage(), "400");
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleIllegalStateException(IllegalStateException ex) {
        log.warn("Conflict: {}. handleIllegalStateException: message={}", ex.getMessage(), ex.getMessage());
        return new ErrorDto(ex.getMessage(), "409");
    }

    @ExceptionHandler(SongNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleSongNotFoundException(SongNotFoundException ex) {
        log.warn("Song Not Found: {}. handleSongNotFoundException: message={}", ex.getMessage(), ex.getMessage());
        return new ErrorDto(ex.getMessage(), "404");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleGlobalException(Exception ex, WebRequest request) {
        log.error("Internal Server Error: {}. handleGlobalException: message={}, request={}", ex.getMessage(), ex.getMessage(), request.getDescription(false));
        return new ErrorDto(ex.getMessage(), "500");
    }
}
