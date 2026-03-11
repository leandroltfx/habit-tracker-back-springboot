package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteHabitUseCaseTest {

    private HabitRepository habitRepository;
    private DeleteHabitUseCase deleteHabitUseCase;

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        deleteHabitUseCase = new DeleteHabitUseCase(habitRepository);
    }

    @Test
    void shouldDeleteHabitWhenUserIsOwner() {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        HabitEntity habit = HabitEntity.builder()
                .id(habitId)
                .userId(userId)
                .build();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.of(habit));

        deleteHabitUseCase.execute(userId, habitId);

        verify(habitRepository).deleteById(habitId);
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

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.of(habit));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> deleteHabitUseCase.execute(userId, habitId)
        );

        assertEquals(
                "Usuário sem autorização para excluir este hábito.",
                exception.getMessage()
        );

        verify(habitRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenHabitDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        when(habitRepository.findById(habitId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> deleteHabitUseCase.execute(userId, habitId)
        );

        assertEquals("Hábito não encontrado.", exception.getMessage());

        verify(habitRepository, never()).deleteById(any());
    }
}