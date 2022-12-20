package com.example.lesson3_food_delivery_app_api.config;

import com.example.lesson3_food_delivery_app_api.entity.EventLog;
import com.example.lesson3_food_delivery_app_api.entity.User;
import com.example.lesson3_food_delivery_app_api.exception.UserLockedException;
import com.example.lesson3_food_delivery_app_api.exception.WrongUsernamePasswordException;
import com.example.lesson3_food_delivery_app_api.security.Role;
import com.example.lesson3_food_delivery_app_api.service.EventLogService;
import com.example.lesson3_food_delivery_app_api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationEvent {
    private final UserService userService;
    private final EventLogService eventLogService;

    @EventListener
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        String userEmail = event.getAuthentication().getName();
        User user = userService.getUserByEmail(userEmail);
        if (user.isLocked()) {
            throw new UserLockedException("Account is locked");
        }
        userService.resetLoginFailAttemptCount(userEmail);
    }

    @EventListener
    public void onLoginFailure(AbstractAuthenticationFailureEvent event) {
        String userEmail = event.getAuthentication().getName();
        User user = userService.getUserByEmail(userEmail);

        // admin user cannot be locked by brute force
        if (user.getRole() != Role.ADMIN) {
            eventLogService.saveEventLog(EventLog.Event.LOGIN_FAILED, user.getId());
            user.setAccessFailedCount(user.getAccessFailedCount() + 1);

            // after 5 login attempts, user will be locked
            if (user.getAccessFailedCount() >= 5) {
                eventLogService.saveEventLog(EventLog.Event.BRUTE_FORCE, user.getId());
                user.setLocked(true);
            }
            userService.saveUser(user);
        }
        throw new WrongUsernamePasswordException("Wrong username or password");
    }
}
