package com.joboffers.domain;

import org.springframework.http.HttpStatus;

public record ErrorMessageResponse(
        String message,
        HttpStatus httpStatus
) {
}
