package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateHabitUseCaseTest {

    private HabitRepository habitRepository;
    private UpdateHabitUseCase updateHabitUseCase;

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        updateHabitUseCase = new UpdateHabitUseCase(habitRepository);
    }

    @Test
    void shouldUpdateHabitSuccessfully() {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        HabitEntity existingHabit = HabitEntity.builder()
                .id(habitId)
                .name("Antigo")
                .description("Descrição antiga")
                .active(false)
                .userId(userId)
                .build();

        CreateHabitRequestDTO request = CreateHabitRequestDTO.builder()
                .name("Novo nome")
                .description("Nova descrição")
                .active(true)
                .build();

        HabitEntity updatedHabit = HabitEntity.builder()
                .id(habitId)
                .name(request.getName())
                .description(request.getDescription())
                .active(request.isActive())
                .userId(userId)
                .build();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.of(existingHabit));

        when(habitRepository.save(any(HabitEntity.class)))
                .thenReturn(updatedHabit);

        CreateHabitResponseDTO response =
                updateHabitUseCase.execute(userId, habitId, request);

        assertNotNull(response);
        assertEquals(habitId, response.getId());
        assertEquals("Novo nome", response.getName());
        assertEquals("Nova descrição", response.getDescription());
        assertTrue(response.isActive());

        verify(habitRepository).findById(habitId);
        verify(habitRepository).save(any(HabitEntity.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUserIsNotOwner() {

        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        HabitEntity habit = HabitEntity.builder()
                .id(habitId)
                .userId(otherUserId)
                .build();

        CreateHabitRequestDTO request = CreateHabitRequestDTO.builder()
                .name("Novo")
                .description("Nova")
                .active(true)
                .build();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.of(habit));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> updateHabitUseCase.execute(userId, habitId, request)
        );

        assertEquals(
                "Usuário sem autorização para alterar este hábito.",
                exception.getMessage()
        );

        verify(habitRepository).findById(habitId);
        verify(habitRepository, never()).save(any());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenHabitDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        CreateHabitRequestDTO request = CreateHabitRequestDTO.builder()
                .name("Novo")
                .description("Nova")
                .active(true)
                .build();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> updateHabitUseCase.execute(userId, habitId, request)
        );

        assertEquals("Hábito não encontrado.", exception.getMessage());

        verify(habitRepository).findById(habitId);
        verify(habitRepository, never()).save(any());
    }
}