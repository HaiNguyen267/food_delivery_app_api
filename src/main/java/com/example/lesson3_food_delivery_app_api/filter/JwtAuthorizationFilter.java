package com.example.lesson3_food_delivery_app_api.filter;

import com.example.lesson3_food_delivery_app_api.exception.UserLockedException;
import com.example.lesson3_food_delivery_app_api.jwt.JwtProvider;
import com.example.lesson3_food_delivery_app_api.security.UserPrincipal;
import com.example.lesson3_food_delivery_app_api.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        Claims claims = JwtProvider.getClaims(token);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        if (checkIfUserWasLocked(email)) {
            throw new UserLockedException("Account is locked");
        }
        UserPrincipal userPrincipal = new UserPrincipal(email, role);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean checkIfUserWasLocked(String email) {
        return userService.checkIfUserWasLocked(email);
    }
}
