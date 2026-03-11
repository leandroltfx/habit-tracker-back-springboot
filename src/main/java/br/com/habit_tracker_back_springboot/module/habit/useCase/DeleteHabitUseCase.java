package br.com.habit_tracker_back_springboot.module.habit.useCase;

import br.com.habit_tracker_back_springboot.module.habit.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeleteHabitUseCase {

    private final HabitRepository habitRepository;

    public void execute(
            UUID userId,
            UUID habitId
    ) {

        habitRepository.findById(habitId).ifPresentOrElse(
                habit -> {
                    if (habit.getUserId().equals(userId)) {
                        habitRepository.deleteById(habitId);
                    } else {
                        throw new AccessDeniedException("Usuário sem autorização para excluir este hábito.");
                    }
                },
                () -> {
                    throw new EntityNotFoundException("Hábito não encontrado.");
                }
        );
    }

}
