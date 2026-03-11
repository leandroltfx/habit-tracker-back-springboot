package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateHabitUseCaseTest {

    private HabitRepository habitRepository;
    private CreateHabitUseCase createHabitUseCase;

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        createHabitUseCase = new CreateHabitUseCase(habitRepository);
    }

    @Test
    void shouldCreateHabitSuccessfully() {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        CreateHabitRequestDTO request = CreateHabitRequestDTO.builder()
                .name("Beber água")
                .description("Beber 2L de água por dia")
                .active(true)
                .build();

        HabitEntity savedHabit = HabitEntity.builder()
                .id(habitId)
                .name(request.getName())
                .description(request.getDescription())
                .active(request.isActive())
                .userId(userId)
                .build();

        when(habitRepository.save(any(HabitEntity.class))).thenReturn(savedHabit);

        CreateHabitResponseDTO response =
                createHabitUseCase.createHabit(userId, request);

        assertNotNull(response);
        assertEquals(habitId, response.getId());
        assertEquals("Beber água", response.getName());
        assertEquals("Beber 2L de água por dia", response.getDescription());
        assertTrue(response.isActive());

        verify(habitRepository).save(any(HabitEntity.class));
    }
}