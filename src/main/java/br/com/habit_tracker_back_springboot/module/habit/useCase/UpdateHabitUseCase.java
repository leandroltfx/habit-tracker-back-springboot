package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UpdateHabitUseCase {

    private final HabitRepository habitRepository;

    public CreateHabitResponseDTO execute(
            UUID userId,
            UUID habitId,
            CreateHabitRequestDTO createHabitRequestDTO
    ) {

        CreateHabitResponseDTO createHabitResponseDTO = new CreateHabitResponseDTO();
        habitRepository.findById(habitId).ifPresentOrElse(
                habitEntity -> {
                    if (habitEntity.getUserId().equals(userId)) {

                        HabitEntity habitEntityUpgradeable = HabitEntity
                                .builder()
                                .id(habitEntity.getId())
                                .name(createHabitRequestDTO.getName())
                                .description(createHabitRequestDTO.getDescription())
                                .active(createHabitRequestDTO.isActive())
                                .createdAt(habitEntity.getCreatedAt())
                                .userId(habitEntity.getUserId())
                                .build();

                        var habitEntityUpdated = this.habitRepository.save(habitEntityUpgradeable);

                        createHabitResponseDTO.setId(habitEntityUpdated.getId());
                        createHabitResponseDTO.setName(createHabitRequestDTO.getName());
                        createHabitResponseDTO.setDescription(createHabitRequestDTO.getDescription());
                        createHabitResponseDTO.setActive(createHabitRequestDTO.isActive());
                        createHabitResponseDTO.setCreatedAt(habitEntityUpdated.getCreatedAt());
                    } else {
                        throw new AccessDeniedException("Usuário sem autorização para alterar este hábito.");
                    }
                },
                () -> {
                    throw new EntityNotFoundException("Hábito não encontrado.");
                }
        );
        return createHabitResponseDTO;
    }

}
