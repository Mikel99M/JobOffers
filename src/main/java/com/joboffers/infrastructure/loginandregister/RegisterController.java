package com.joboffers.infrastructure.loginandregister;

import com.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import com.joboffers.domain.loginandregister.RegisterRequestDto;
import com.joboffers.domain.loginandregister.RegistrationResultDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade facade;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        RegistrationResultDto registerResult = facade.register(
                new RegisterRequestDto(registerRequest.username(), registerRequest.password())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResult);
    }
}