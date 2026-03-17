package com.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
public record RegisterRequestDto(
        String username,
        String password
) {
}
