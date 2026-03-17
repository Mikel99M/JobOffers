package com.joboffers.domain.loginandregister;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginAndRegisterFacadeTest {

    private UserRepositoryStub stubRepo;
    private LoginAndRegisterFacade facade;

    @BeforeEach
    void setup() {
        stubRepo = new UserRepositoryStub();
        facade = new LoginAndRegisterFacade(stubRepo);
    }

    @Test
    void should_Register_User_When_User_Does_Not_Exist() {
        // given
        RegisterRequestDto requestDto = new RegisterRequestDto("John", "pass");

        // when
        facade.register(requestDto);

        // then
        User user = stubRepo.findByEmail("john@example.com").orElseThrow();
        assertThat("John").isEqualTo(user.getUsername());
        assertThat("pass").isEqualTo(user.getPassword());
    }

    @Test
    void should_Not_Register_When_User_Exists() {
        // given
        User existing = User.builder()
                .email("john@example.com")
                .userName("Old John")
                .password("old")
                .build();

        stubRepo.save(existing);
        RegisterRequestDto req = new RegisterRequestDto("John", "pass");

        // when
        facade.register(req);

        // then
        User user = stubRepo.findByEmail("john@example.com").orElseThrow();
        assertThat("Old John").isEqualTo(user.getUsername());   // not overwritten
        assertThat("old").isEqualTo(user.getPassword());
    }

    @Test
    void shouldDeleteUserWhenPasswordMatches() {
        // given
        User user = User.builder()
                .email("john@example.com")
                .password("pass")
                .userName("John")
                .build();
        stubRepo.save(user);

        DeleteRequestDto req = new DeleteRequestDto( "pass", "john@example.com");

        // when
        facade.deleteUser(req);

        // then
        assertThat(stubRepo.existsByUserName("john@example.com"));
    }

    @Test
    void shouldNotDeleteWhenPasswordDoesNotMatch() {
        // given
        User user = User.builder()
                .email("john@example.com")
                .password("pass")
                .userName("John")
                .build();
        stubRepo.save(user);

        DeleteRequestDto req = new DeleteRequestDto( "pass", "john@example.com");

        // when
        facade.deleteUser(req);

        // then
        assertThat(stubRepo.existsByUserName("john@example.com"));
    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        // when & then
        assertThatThrownBy(() -> facade.findByEmail("john@example.com"))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        User user = User.builder()
                .email("john@example.com")
                .userName("John")
                .password("pass")
                .build();
        stubRepo.save(user);

        // when
        UserDto dto = facade.findByEmail("john@example.com");

        // then
        assertThat("John").isEqualTo(dto.name());
        assertThat("john@example.com").isEqualTo(dto.email());
    }

    @Test
    void shouldThrowWhenUserNotFoundByEmail() {
        // when & then
        assertThatThrownBy(() -> facade.findByEmail("john@example.com"))
                .isInstanceOf(BadCredentialsException.class);    }
}