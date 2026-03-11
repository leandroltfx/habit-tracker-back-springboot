package br.com.habit_tracker_back_springboot.module.habit.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateHabitRequestDTO {

    @Size(min = 3, max = 100, message = "O nome do hábito deve ter entre 3 e 100 caracteres.")
    private String name;

    @Size(max = 255, message = "A descrição não deve ultrapassar 255 caracateres.")
    private String description;

    private boolean active;

}
