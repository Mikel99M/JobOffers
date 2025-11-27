package com.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
record RegisterRequestDto(
        String name,
        String password,
        String email
) {
}
