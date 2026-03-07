package br.com.habit_tracker_back_springboot.module.login.controller;

import br.com.habit_tracker_back_springboot.module.login.dto.LoginRequestDTO;
import br.com.habit_tracker_back_springboot.module.login.dto.LoginResponseDTO;
import br.com.habit_tracker_back_springboot.module.login.useCase.LoginUseCase;
import br.com.habit_tracker_back_springboot.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.security.auth.login.CredentialException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("leandro")
                .password("123456")
                .build();

        LoginResponseDTO response = LoginResponseDTO.builder()
                .accessToken("jwt-token")
                .build();

        when(loginUseCase.execute(any())).thenReturn(response);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.data.accessToken")
                        .value("jwt-token"));
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("leandro")
                .password("wrong")
                .build();

        when(loginUseCase.execute(any()))
                .thenThrow(new CredentialException("Credenciais inválidas"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
