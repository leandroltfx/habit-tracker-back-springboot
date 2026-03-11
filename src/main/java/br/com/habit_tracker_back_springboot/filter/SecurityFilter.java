package br.com.habit_tracker_back_springboot.filter;

import br.com.habit_tracker_back_springboot.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null) {
            var subjectToken = this.jwtService.validateToken(header);
            if (subjectToken.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            request.setAttribute("user_id", subjectToken);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    subjectToken,
                    null,
                    Collections.emptyList()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}

