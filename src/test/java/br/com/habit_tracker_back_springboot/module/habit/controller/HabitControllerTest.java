package br.com.habit_tracker_back_springboot.module.habit.controller;

import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.HabitDTO;
import br.com.habit_tracker_back_springboot.module.habit.useCase.CreateHabitUseCase;
import br.com.habit_tracker_back_springboot.module.habit.useCase.DeleteHabitUseCase;
import br.com.habit_tracker_back_springboot.module.habit.useCase.ListHabitsUseCase;
import br.com.habit_tracker_back_springboot.module.habit.useCase.UpdateHabitUseCase;
import br.com.habit_tracker_back_springboot.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HabitController.class)
@AutoConfigureMockMvc(addFilters = false)
class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateHabitUseCase createHabitUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private DeleteHabitUseCase deleteHabitUseCase;

    @MockBean
    private ListHabitsUseCase listHabitsUseCase;

    @MockBean
    private UpdateHabitUseCase updateHabitUseCase;

    @Test
    void shouldCreateHabitSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        CreateHabitRequestDTO request = CreateHabitRequestDTO.builder()
                .name("Beber água")
                .description("Beber 2L por dia")
                .active(true)
                .build();

        CreateHabitResponseDTO response = CreateHabitResponseDTO.builder()
                .id(habitId)
                .name("Beber água")
                .description("Beber 2L por dia")
                .active(true)
                .build();

        when(createHabitUseCase.execute(eq(userId), any()))
                .thenReturn(response);

        mockMvc.perform(post("/habits")
                        .requestAttr("user_id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Hábito cadastrado com sucesso!"))
                .andExpect(jsonPath("$.data.id")
                        .value(habitId.toString()))
                .andExpect(jsonPath("$.data.name")
                        .value("Beber água"))
                .andExpect(jsonPath("$.data.description")
                        .value("Beber 2L por dia"))
                .andExpect(jsonPath("$.data.active")
                        .value(true));
    }

    @Test
    void shouldDeleteHabitSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        doNothing().when(deleteHabitUseCase)
                .execute(userId, habitId);

        mockMvc.perform(delete("/habits/{habit_id}", habitId)
                        .requestAttr("user_id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Hábito excluído com sucesso!"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void shouldListHabitsSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();

        List<HabitDTO> habits = List.of(
                HabitDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Beber água")
                        .description("Beber 2L por dia")
                        .active(true)
                        .build()
        );

        when(listHabitsUseCase.execute(userId))
                .thenReturn(habits);

        mockMvc.perform(get("/habits")
                        .requestAttr("user_id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").doesNotExist())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldUpdateHabitSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID habitId = UUID.randomUUID();

        CreateHabitRequestDTO request = CreateHabitRequestDTO.builder()
                .name("Treinar")
                .description("Treinar todos os dias")
                .active(true)
                .build();

        CreateHabitResponseDTO response = CreateHabitResponseDTO.builder()
                .id(habitId)
                .name("Treinar")
                .description("Treinar todos os dias")
                .active(true)
                .build();

        when(updateHabitUseCase.execute(eq(userId), eq(habitId), any()))
                .thenReturn(response);

        mockMvc.perform(put("/habits/{habit_id}", habitId)
                        .requestAttr("user_id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Hábito alterado com sucesso!"))
                .andExpect(jsonPath("$.data.id").value(habitId.toString()))
                .andExpect(jsonPath("$.data.name").value("Treinar"));
    }
}