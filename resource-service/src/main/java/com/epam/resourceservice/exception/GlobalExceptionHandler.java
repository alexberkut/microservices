package com.epam.resourceservice.exception;

import com.epam.resourceservice.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid value '%s' for ID. Must be a positive integer", ex.getValue());
        log.warn("Argument Type Mismatch: {}. value={}", ex.getMessage(), ex.getValue());
        return new ErrorDto(errorMessage, "400");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Bad Request: {}", ex.getMessage());
        return new ErrorDto(ex.getMessage(), "400");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource Not Found: {}", ex.getMessage());
        return new ErrorDto(ex.getMessage(), "404");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        var errorMessage = String.format("Invalid file format: %s. Only MP3 files are allowed", ex.getContentType());
        log.warn("Media Type Not Supported: {}", ex.getMessage());
        return new ErrorDto(errorMessage, "400");
    }

    @ExceptionHandler(SongServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorDto handleSongServiceUnavailableException(SongServiceUnavailableException ex) {
        log.error("Song Service is unavailable: {}", ex.getMessage());
        return new ErrorDto("Song service is currently unavailable", "503");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleGlobalException(Exception ex) {
        log.error("An unexpected internal server error occurred: {}", ex.getMessage(), ex);
        return new ErrorDto("An unexpected internal server error occurred", "500");
    }
}
