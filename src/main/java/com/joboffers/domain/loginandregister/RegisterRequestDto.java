package com.joboffers.domain.loginandregister;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import javax.validation.constraints.NotBlank;

@Builder
public record RegisterRequestDto(
        @JsonAlias("userName")
        @NotBlank(message = "{username.not.blank}")
        String username,
        @NotBlank(message = "{password.not.blank}")
        String password
) {
}
