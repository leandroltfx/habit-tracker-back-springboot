package br.com.habit_tracker_back_springboot.module.habit.controller;

import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitRequestDTO;
import br.com.habit_tracker_back_springboot.module.habit.dto.CreateHabitResponseDTO;
import br.com.habit_tracker_back_springboot.module.habit.useCase.CreateHabitUseCase;
import br.com.habit_tracker_back_springboot.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        when(createHabitUseCase.createHabit(eq(userId), any()))
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
}