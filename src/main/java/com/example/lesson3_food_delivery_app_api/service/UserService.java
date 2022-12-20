package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.AdminRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.CustomerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.DeliveryPartnerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.LoginResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.exception.WrongUsernamePasswordException;
import com.example.lesson3_food_delivery_app_api.repository.*;
import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest.*;

@Service
@AllArgsConstructor
public class UserService {
    // repositories
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final RestaurantRepository restaurantRepository;
    private final EventLogService eventLogService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<?> changeAccess(ChangeAccessRequest changeAccessRequest) {
        User user = getUserById(changeAccessRequest.getUserId());
        Operation operation = changeAccessRequest.getOperation();


        if (operation == Operation.LOCK && user.isLocked()) {
            // if admin try to lock user which is already locked
            ErrorResponse response = new ErrorResponse("User is already locked");
            return ResponseEntity.badRequest().body(response);
        } else if (operation == Operation.UNLOCK && !user.isLocked()) {
            // if admin try unlock user who is not locked
            ErrorResponse response = new ErrorResponse("User is not locked");
            return ResponseEntity.badRequest().body(response);
        }

        EventLog.Event event = null;
        if (changeAccessRequest.getOperation() == Operation.LOCK) {
            boolean userIsAdmin = user.getRole().equals(Role.ADMIN);
            if (userIsAdmin) {
                ErrorResponse response = new ErrorResponse("You can't lock admin");
                return ResponseEntity.badRequest().body(response);
            }
            event = EventLog.Event.LOCK_USER;
            user.setLocked(true);
        } else {
            user.setLocked(false);
            event = EventLog.Event.UNLOCK_USER;
        }


        user = userRepository.save(user);
        eventLogService.saveEventLog(event, user.getId());
        SuccessResponse successResponse = new SuccessResponse(String.format("User %s %sed successfully", user.getEmail(), operation.name().toLowerCase()));
        return ResponseEntity.ok(successResponse);
    }

    @Transactional
    public ResponseEntity<?> registerAdmin(AdminRegistrationRequest adminRegistrationRequest) {
        String email = adminRegistrationRequest.getEmail();
        String password = adminRegistrationRequest.getPassword();

        if (existsByEmail(email)) {
            ErrorResponse errorResponse = new ErrorResponse("Email already registered");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin = adminRepository.save(admin);

        eventLogService.saveEventLog(EventLog.Event.REGISTER, admin.getId());
        return AuthService.createResponseWithAccessToken(admin);

    }

    public ResponseEntity<?> registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String name = customerRegistrationRequest.getName();
        String email = customerRegistrationRequest.getEmail();
        String password = customerRegistrationRequest.getPassword();
        String address = customerRegistrationRequest.getAddress();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            ErrorResponse response = new ErrorResponse("Email already registered");
            return ResponseEntity.badRequest().body(response);
        }

        Customer customer = Customer.builder()
                .name(name)
                .address(address)
                .build();

        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setRole(Role.CUSTOMER);

        customer = customerRepository.save(customer);
        eventLogService.saveEventLog(EventLog.Event.REGISTER, customer.getId());
        return AuthService.createResponseWithAccessToken(customer);

    }

    public ResponseEntity<?> registerDeliveryPartner(DeliveryPartnerRegistrationRequest registrationRequest) {
        String name = registrationRequest.getName();
        String email = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();

        if (existsByEmail(email)) {
            ErrorResponse response = new ErrorResponse("Email already registered");
            return ResponseEntity.badRequest().body(response);
        }

        DeliveryPartner deliveryPartner = DeliveryPartner.builder()
                .name(name)
                .build();

        deliveryPartner.setEmail(email);
        deliveryPartner.setPassword(passwordEncoder.encode(password));
        deliveryPartner.setRole(Role.DELIVERY_PARTNER);

        deliveryPartner = deliveryPartnerRepository.save(deliveryPartner);
        eventLogService.saveEventLog(EventLog.Event.REGISTER, deliveryPartner.getId());
        SuccessResponse response = new SuccessResponse("Delivery partner registered successfully");
        return AuthService.createResponseWithAccessToken(deliveryPartner);
    }


    public ResponseEntity<?> registerRestaurant(RestaurantRegistrationRequest registrationRequest) {
        String restaurantEmail = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();
        String address = registrationRequest.getAddress();
        String phone = registrationRequest.getPhone();
        String name = registrationRequest.getName();

        if (userRepository.existsByEmailIgnoreCase(restaurantEmail)) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .build();

        restaurant.setEmail(restaurantEmail);
        restaurant.setPassword(passwordEncoder.encode(password));
        restaurant.setRole(Role.RESTAURANT);

        restaurant = restaurantRepository.save(restaurant);
        eventLogService.saveEventLog(EventLog.Event.REGISTER, restaurant.getId());
        return AuthService.createResponseWithAccessToken(restaurant);
    }




    public User getUserById(long userId) {
        // TODO: NOT FOUND EXCEPTION
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByEmail(String username) {
        return userRepository.existsByEmailIgnoreCase(username);
    }

    public boolean checkIfUserWasLocked(String email) {

        User user = getUserByEmail(email);
        return user.isLocked();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new WrongUsernamePasswordException("Wrong username or password"));
    }

    public void resetLoginFailAttemptCount(String userEmail) {
        User user = getUserByEmail(userEmail);
        user.setAccessFailedCount(0);
        saveUser(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


}
