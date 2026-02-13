package com.epam.resourceservice.exception;

public class SongServiceUnavailableException extends RuntimeException {
    public SongServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
