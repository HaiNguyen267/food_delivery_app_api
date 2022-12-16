package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.LoginRequest;
import com.example.lesson3_food_delivery_app_api.entity.User;
import com.example.lesson3_food_delivery_app_api.exception.WrongUsernamePasswordException;
import com.example.lesson3_food_delivery_app_api.jwt.JwtProvider;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public String login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (authentication.isAuthenticated()) {
            System.out.println("Authentication is successful");
            User user = userRepository.findByEmail(loginRequest.getUsername()).get();
            return JwtProvider.generateToken(user);
        } else {
            throw new WrongUsernamePasswordException("Wrong username or password");
        }
    }
}
