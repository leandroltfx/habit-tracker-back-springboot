package br.com.habit_tracker_back_springboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;

    private final String secret = "test-secret";
    private final String issuer = "test-issuer";

    @BeforeEach
    void setup() {
        jwtService = new JWTService();

        ReflectionTestUtils.setField(jwtService, "jwtTokenSecret", secret);
        ReflectionTestUtils.setField(jwtService, "jwtTokenIssuer", issuer);
    }

    @Test
    void shouldGenerateValidToken() {
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateToken(userId);

        String subject = jwtService.validateToken(token);

        assertEquals(userId.toString(), subject);
    }

    @Test
    void shouldValidateTokenWithBearerPrefix() {
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateToken(userId);

        String subject = jwtService.validateToken("Bearer " + token);

        assertEquals(userId.toString(), subject);
    }

    @Test
    void shouldReturnEmptyStringWhenTokenIsInvalid() {
        String invalidToken = "token.invalido.qualquer";

        String subject = jwtService.validateToken(invalidToken);

        assertEquals("", subject);
    }

}