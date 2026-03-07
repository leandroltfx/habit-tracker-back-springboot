package br.com.habit_tracker_back_springboot.module.login.useCase;

import br.com.habit_tracker_back_springboot.module.login.dto.LoginRequestDTO;
import br.com.habit_tracker_back_springboot.module.login.dto.LoginResponseDTO;
import br.com.habit_tracker_back_springboot.module.user.repository.UserRepository;
import br.com.habit_tracker_back_springboot.service.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;

@Service
@AllArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public LoginResponseDTO execute(
            LoginRequestDTO loginRequestDTO
    ) throws CredentialException {
        var user = this.userRepository.findByUsernameOrEmail(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getEmail()
        ).orElseThrow(() -> new CredentialException("Credenciais inválidas"));

        var passwordMatches = this.passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new CredentialException("Credenciais inválidas");
        }

        return LoginResponseDTO
                .builder()
                .accessToken(this.jwtService.generateToken(user.getId()))
                .build();
    }

}
