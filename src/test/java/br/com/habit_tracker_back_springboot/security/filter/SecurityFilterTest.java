package br.com.habit_tracker_back_springboot.security.filter;

import br.com.habit_tracker_back_springboot.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class SecurityFilterTest {

    private JWTService jwtService;
    private SecurityFilter securityFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        jwtService = mock(JWTService.class);
        securityFilter = new SecurityFilter(jwtService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterWhenAuthorizationHeaderIsNull() throws Exception {

        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid");
        when(jwtService.validateToken("Bearer invalid")).thenReturn("");

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateUserWhenTokenIsValid() throws Exception {

        String userId = "user-123";

        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.validateToken("Bearer validToken")).thenReturn(userId);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("userId", userId);
        verify(filterChain).doFilter(request, response);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        assert authentication.getPrincipal().equals(userId);
        assert authentication.isAuthenticated();
    }

}