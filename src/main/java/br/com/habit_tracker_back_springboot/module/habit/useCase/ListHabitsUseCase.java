package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.dto.HabitDTO;
import br.com.habit_tracker_back_springboot.module.habit.entity.HabitEntity;
import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ListHabitsUseCase {

    private final HabitRepository habitRepository;

    public List<HabitDTO> execute(
            UUID userId
    ) {

        var listHabitEntity = this.habitRepository.findAllByUserId(userId);

        if (listHabitEntity.isEmpty()) {
            return Collections.emptyList();
        }

        List<HabitDTO> listHabitDTO = new ArrayList<HabitDTO>();
        for (HabitEntity habitEntity : listHabitEntity) {
            HabitDTO habitDTO = HabitDTO
                    .builder()
                    .id(habitEntity.getId())
                    .name(habitEntity.getName())
                    .description(habitEntity.getDescription())
                    .active(habitEntity.isActive())
                    .createdAt(habitEntity.getCreatedAt())
                    .build();
            listHabitDTO.add(habitDTO);
        }

        return listHabitDTO;
    }

}
