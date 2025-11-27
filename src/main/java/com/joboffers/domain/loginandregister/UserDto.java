package com.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
record UserDto(
        String name,
        String email

) {
}
