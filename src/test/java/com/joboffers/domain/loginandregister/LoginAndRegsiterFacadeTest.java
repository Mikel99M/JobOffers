package com.joboffers.domain.loginandregister;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginAndRegsiterFacadeTest {

    private UserRepositoryStub stubRepo;
    private LoginAndRegisterFacade facade;

    @BeforeEach
    void setup() {
        stubRepo = new UserRepositoryStub();
        facade = new LoginAndRegisterFacade(stubRepo);
    }

    @Test
    void should_Register_User_When_Email_Does_Not_Exist() {
        // given
        RegisterRequestDto requestDto = new RegisterRequestDto("John", "pass", "john@example.com");

        // when
        facade.register(requestDto);

        // then
        User user = stubRepo.findByEmail("john@example.com").orElseThrow();
        assertThat("John").isEqualTo(user.getName());
        assertThat("pass").isEqualTo(user.getPassword());
    }

    @Test
    void shouldNotRegisterWhenEmailExists() {
        // given
        User existing = User.builder()
                .email("john@example.com")
                .name("Old John")
                .password("old")
                .build();

        stubRepo.save(existing);
        RegisterRequestDto req = new RegisterRequestDto("John", "pass", "john@example.com");

        // when
        facade.register(req);

        // then
        User user = stubRepo.findByEmail("john@example.com").orElseThrow();
        assertThat("Old John").isEqualTo(user.getName());   // not overwritten
        assertThat("old").isEqualTo(user.getPassword());
    }

    @Test
    void shouldDeleteUserWhenPasswordMatches() {
        // given
        User user = User.builder()
                .email("john@example.com")
                .password("pass")
                .name("John")
                .build();
        stubRepo.save(user);

        DeleteRequestDto req = new DeleteRequestDto( "pass", "john@example.com");

        // when
        facade.deleteUser(req);

        // then
        assertThat(stubRepo.existsByEmail("john@example.com"));
    }

    @Test
    void shouldNotDeleteWhenPasswordDoesNotMatch() {
        // given
        User user = User.builder()
                .email("john@example.com")
                .password("pass")
                .name("John")
                .build();
        stubRepo.save(user);

        DeleteRequestDto req = new DeleteRequestDto( "pass", "john@example.com");

        // when
        facade.deleteUser(req);

        // then
        assertThat(stubRepo.existsByEmail("john@example.com"));
    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        DeleteRequestDto req = new DeleteRequestDto("pass", "John@example.com");

        assertThatThrownBy(() -> facade.findByEmail("john@example.com"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        User user = User.builder()
                .email("john@example.com")
                .name("John")
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
        assertThatThrownBy(() -> facade.findByEmail("john@example.com"))
                .isInstanceOf(UserNotFoundException.class);    }
}