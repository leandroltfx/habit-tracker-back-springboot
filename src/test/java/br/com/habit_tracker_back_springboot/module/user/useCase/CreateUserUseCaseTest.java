package br.com.habit_tracker_back_springboot.module.user.useCase;

import br.com.habit_tracker_back_springboot.module.user.dto.CreateUserRequestDTO;
import br.com.habit_tracker_back_springboot.module.user.dto.CreateUserResponseDTO;
import br.com.habit_tracker_back_springboot.module.user.entity.UserEntity;
import br.com.habit_tracker_back_springboot.module.user.repository.UserRepository;
import br.com.habit_tracker_back_springboot.service.JWTService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTService jwtService;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JWTService.class);

        createUserUseCase = new CreateUserUseCase(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {

        UUID userId = UUID.randomUUID();

        CreateUserRequestDTO request = CreateUserRequestDTO
                .builder()
                .username("leandro")
                .email("leandro@email.com")
                .password("123456")
                .build();

        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");

        UserEntity savedUser = UserEntity.builder()
                .id(userId)
                .username("leandro")
                .email("leandro@email.com")
                .password("encodedPassword")
                .build();

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(jwtService.generateToken(userId)).thenReturn("jwt-token");

        CreateUserResponseDTO response = createUserUseCase.execute(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());

        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(UserEntity.class));
        verify(jwtService).generateToken(userId);
    }

    @Test
    void shouldEncodePasswordBeforeSavingUser() {

        CreateUserRequestDTO request = CreateUserRequestDTO
                .builder()
                .username("user")
                .email("user@email.com")
                .password("123")
                .build();

        when(passwordEncoder.encode("123")).thenReturn("encoded");

        UserEntity savedUser = UserEntity.builder()
                .id(UUID.randomUUID())
                .build();

        when(userRepository.save(any())).thenReturn(savedUser);
        when(jwtService.generateToken(any())).thenReturn("token");

        createUserUseCase.execute(request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity capturedUser = captor.getValue();

        assertEquals("encoded", capturedUser.getPassword());
    }

    @Test
    void shouldThrowEntityExistsExceptionWhenRepositoryFails() {

        CreateUserRequestDTO request = CreateUserRequestDTO
                .builder()
                .username("user")
                .email("user@email.com")
                .password("123")
                .build();

        when(passwordEncoder.encode(any())).thenReturn("encoded");

        when(userRepository.save(any()))
                .thenThrow(new RuntimeException());

        EntityExistsException exception = assertThrows(
                EntityExistsException.class,
                () -> createUserUseCase.execute(request)
        );

        assertEquals(
                "Este nome de usuário ou e-mail já está em uso.",
                exception.getMessage()
        );
    }
}