package br.com.habit_tracker_back_springboot.module.login.controller;

import br.com.habit_tracker_back_springboot.dto.ApiResponseDTO;
import br.com.habit_tracker_back_springboot.module.login.dto.LoginRequestDTO;
import br.com.habit_tracker_back_springboot.module.login.useCase.LoginUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO
    ) throws CredentialException {
        var result = this.loginUseCase.execute(loginRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDTO
                                .builder()
                                .message("Login realizado com sucesso!")
                                .data(result)
                                .build()
                );
    }

}
