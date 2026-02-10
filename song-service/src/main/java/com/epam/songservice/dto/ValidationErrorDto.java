package com.epam.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDto {
    private String errorMessage;
    private Map<String, String> details;
    private String errorCode;
}
