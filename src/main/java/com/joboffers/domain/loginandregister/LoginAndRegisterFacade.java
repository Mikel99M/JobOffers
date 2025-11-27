package com.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final UserRepository userRepository;

    public void register(RegisterRequestDto requestDto) {

        if (!userRepository.existsByEmail(requestDto.email())) {
            User user = User.builder()
                    .email(requestDto.email())
                    .password(requestDto.password())
                    .name(requestDto.name())
                    .build();
            userRepository.save(user);
        }
    }

    public void deleteUser(DeleteRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email()).orElseThrow(() -> new UserNotFoundException("User with email \"%s\" not found.".formatted(requestDto.email())));
        if (requestDto.password().equals(user.getPassword())) {
            userRepository.delete(user);
        }
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email \"%s\" not found.".formatted(email)));
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

}
