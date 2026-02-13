package com.epam.songservice.validation;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SongValidator {

    public void validateId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            long num = Long.parseLong(id);
            if (num <= 0) {
                throw new IllegalArgumentException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
        }
    }

    public void validateIds(String ids) {
        if (ids == null || ids.isBlank()) {
            throw new IllegalArgumentException("ID list cannot be empty");
        }
        if (ids.length() > 200) {
            throw new IllegalArgumentException(String.format("CSV string is too long: received %d characters, maximum allowed is 200", ids.length()));
        }

        Arrays.stream(ids.split(","))
                .map(String::trim)
                .forEach(idStr -> {
                    try {
                        long num = Long.parseLong(idStr);
                        if (num <= 0) {
                            throw new IllegalArgumentException(String.format("Invalid ID format: '%s'. Only positive integers are allowed", idStr));
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format("Invalid ID format: '%s'. Only positive integers are allowed", idStr));
                    }
                });
    }
}
