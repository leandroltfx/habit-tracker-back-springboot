package br.com.habit_tracker_back_springboot.module.login.useCase;

import br.com.habit_tracker_back_springboot.module.login.dto.LoginRequestDTO;
import br.com.habit_tracker_back_springboot.module.login.dto.LoginResponseDTO;
import br.com.habit_tracker_back_springboot.module.user.entity.UserEntity;
import br.com.habit_tracker_back_springboot.module.user.repository.UserRepository;
import br.com.habit_tracker_back_springboot.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.CredentialException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTService jwtService;

    private LoginUseCase loginUseCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JWTService.class);

        loginUseCase = new LoginUseCase(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("leandro")
                .password("123456")
                .build();

        UserEntity user = UserEntity.builder()
                .id(userId)
                .username("leandro")
                .password("encodedPassword")
                .build();

        when(userRepository.findByUsernameOrEmail("leandro", null))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(userId))
                .thenReturn("jwt-token");

        LoginResponseDTO response = loginUseCase.execute(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());

        verify(userRepository).findByUsernameOrEmail("leandro", null);
        verify(passwordEncoder).matches("123456", "encodedPassword");
        verify(jwtService).generateToken(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("leandro")
                .password("123456")
                .build();

        when(userRepository.findByUsernameOrEmail("leandro", null))
                .thenReturn(Optional.empty());

        CredentialException exception = assertThrows(
                CredentialException.class,
                () -> loginUseCase.execute(request)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("leandro")
                .password("wrongPassword")
                .build();

        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID())
                .password("encodedPassword")
                .build();

        when(userRepository.findByUsernameOrEmail("leandro", null))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        CredentialException exception = assertThrows(
                CredentialException.class,
                () -> loginUseCase.execute(request)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
    }
}
