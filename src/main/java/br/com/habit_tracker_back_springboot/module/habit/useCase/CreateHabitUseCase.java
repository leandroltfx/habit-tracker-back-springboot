package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CreateHabitUseCase {

    private final HabitRepository habitRepository;

    public CreateHabitResponseDTO execute(
            UUID userId,
            CreateHabitRequestDTO createHabitRequestDTO
    ) {
        HabitEntity habitEntity = HabitEntity
                .builder()
                .name(createHabitRequestDTO.getName())
                .description(createHabitRequestDTO.getDescription())
                .active(createHabitRequestDTO.isActive())
                .userId(userId)
                .build();

        var habitCreated = this.habitRepository.save(habitEntity);

        return CreateHabitResponseDTO
                .builder()
                .id(habitCreated.getId())
                .name(habitCreated.getName())
                .description(habitCreated.getDescription())
                .active(habitCreated.isActive())
                .createdAt(habitCreated.getCreatedAt())
                .build();
    }

}
