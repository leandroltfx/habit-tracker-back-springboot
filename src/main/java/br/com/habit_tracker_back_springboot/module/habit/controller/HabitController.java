package br.com.habit_tracker_back_springboot.module.habit.controller;

import br.com.habit_tracker_back_springboot.dto.ApiResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.useCase.CreateHabitUseCase;
import br.com.habit_tracker_back_springboot.module.habit.useCase.DeleteHabitUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/habits")
@AllArgsConstructor
public class HabitController {

    private final CreateHabitUseCase createHabitUseCase;
    private final DeleteHabitUseCase deleteHabitUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createHabit(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody CreateHabitRequestDTO createHabitRequestDTO
    ) {
        var habitCreated = this.createHabitUseCase.execute(
                this.extractUserIdFromRequest(httpServletRequest),
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

    @DeleteMapping("/{habit_id}")
    public ResponseEntity<ApiResponseDTO> deletehabit(
            HttpServletRequest httpServletRequest,
            @PathVariable("habit_id") String habitId
    ) {
        this.deleteHabitUseCase.execute(
                this.extractUserIdFromRequest(httpServletRequest),
                UUID.fromString(habitId)
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDTO
                                .builder()
                                .message("Hábito excluído com sucesso!")
                                .data(null)
                                .build()
                );
    }

    private UUID extractUserIdFromRequest(
            HttpServletRequest httpServletRequest
    ) {
        var userId = httpServletRequest.getAttribute("user_id");
        return UUID.fromString(userId.toString());
    }

}
