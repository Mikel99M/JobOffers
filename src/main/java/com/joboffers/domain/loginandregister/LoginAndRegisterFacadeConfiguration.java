package com.joboffers.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginAndRegisterFacadeConfiguration {

    @Bean
    LoginAndRegisterFacade loginAndRegisterFacade(UserRepository userRepository) {
        return new LoginAndRegisterFacade(userRepository);
    }
}
