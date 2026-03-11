package br.com.habit_tracker_back_springboot.module.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitDTO {

    private UUID id;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;

}
