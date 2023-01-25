package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.LoginRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.AccessToken;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.EventLog;
import com.example.lesson3_food_delivery_app_api.entity.User;
import com.example.lesson3_food_delivery_app_api.exception.UserLockedException;
import com.example.lesson3_food_delivery_app_api.exception.WrongUsernamePasswordException;
import com.example.lesson3_food_delivery_app_api.jwt.JwtProvider;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final EventLogService eventLogService;
    private final UserRepository userRepository;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);


        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentication.isAuthenticated()) {
            User user = userRepository.findByEmail(loginRequest.getUsername()).get();

            if (user.isLocked()) throw new UserLockedException("Account is locked");

            eventLogService.saveEventLog(EventLog.Event.LOGIN, user.getId());
            AccessToken accessToken = generateAccessToken(user);
            SuccessResponse response = new SuccessResponse(200, "Login successful", accessToken);
            return ResponseEntity.ok(response);
        } else {
            System.out.println("wrong username and password message");
            throw new WrongUsernamePasswordException("Wrong username or password");
        }
    }

    public static AccessToken generateAccessToken(User user) {
        String token = JwtProvider.generateToken(user);
        return new AccessToken(token);
    }
}
