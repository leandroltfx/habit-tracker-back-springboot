package br.com.habit_tracker_back_springboot.module.user.controller;

import br.com.habit_tracker_back_springboot.module.user.dto.CreateUserRequestDTO;
import br.com.habit_tracker_back_springboot.module.user.dto.CreateUserResponseDTO;
import br.com.habit_tracker_back_springboot.module.user.useCase.CreateUserUseCase;
import br.com.habit_tracker_back_springboot.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .username("leandro")
                .email("leandro@email.com")
                .password("12345678")
                .build();

        CreateUserResponseDTO response = CreateUserResponseDTO.builder()
                .accessToken("jwt-token")
                .build();

        when(createUserUseCase.execute(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Usuário cadastrado com sucesso!"))
                .andExpect(jsonPath("$.data.accessToken")
                        .value("jwt-token"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .username("")
                .email("")
                .password("")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}