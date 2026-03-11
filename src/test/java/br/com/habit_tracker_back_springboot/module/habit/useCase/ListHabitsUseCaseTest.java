package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.dto.HabitDTO;
import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListHabitsUseCaseTest {

    private HabitRepository habitRepository;
    private ListHabitsUseCase listHabitsUseCase;

    @BeforeEach
    void setup() {
        habitRepository = mock(HabitRepository.class);
        listHabitsUseCase = new ListHabitsUseCase(habitRepository);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoHabits() {

        UUID userId = UUID.randomUUID();

        when(habitRepository.findAllByUserId(userId))
                .thenReturn(List.of());

        List<HabitDTO> result = listHabitsUseCase.execute(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(habitRepository).findAllByUserId(userId);
    }

    @Test
    void shouldReturnMappedHabitDTOList() {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        HabitEntity habitEntity = HabitEntity.builder()
                .id(habitId)
                .name("Beber água")
                .description("Beber 2L por dia")
                .active(true)
                .userId(userId)
                .build();

        when(habitRepository.findAllByUserId(userId))
                .thenReturn(List.of(habitEntity));

        List<HabitDTO> result = listHabitsUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        HabitDTO habitDTO = result.get(0);

        assertEquals(habitId, habitDTO.getId());
        assertEquals("Beber água", habitDTO.getName());
        assertEquals("Beber 2L por dia", habitDTO.getDescription());
        assertTrue(habitDTO.isActive());

        verify(habitRepository).findAllByUserId(userId);
    }
}