package com.joboffers.infrastracture.loginandregister.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponse(String message, HttpStatus status) {
}
