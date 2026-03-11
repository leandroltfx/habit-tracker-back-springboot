package br.com.habit_tracker_back_springboot.module.habit.controller;

import br.com.habit_tracker_back_springboot.dto.ApiResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.useCase.CreateHabitUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/habits")
@AllArgsConstructor
public class HabitController {

    private final CreateHabitUseCase createHabitUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createHabit(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody CreateHabitRequestDTO createHabitRequestDTO
    ) {

        var userId = httpServletRequest.getAttribute("user_id");

        var habitCreated = this.createHabitUseCase.createHabit(
                UUID.fromString(userId.toString()),
                createHabitRequestDTO
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    ApiResponseDTO
                            .builder()
                            .message("Hábito cadastrado com sucesso!")
                            .data(habitCreated)
                            .build()
                );
    }

}
