package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.AdminRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.CustomerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.DeliveryPartnerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.Admin;
import com.example.lesson3_food_delivery_app_api.entity.Customer;
import com.example.lesson3_food_delivery_app_api.entity.DeliveryPartner;
import com.example.lesson3_food_delivery_app_api.entity.User;
import com.example.lesson3_food_delivery_app_api.repository.AdminRepository;
import com.example.lesson3_food_delivery_app_api.repository.CustomerRepository;
import com.example.lesson3_food_delivery_app_api.repository.DeliveryPartnerRepository;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest.*;

@Service
@AllArgsConstructor
public class UserService {
    // repositories
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;

    private final PasswordEncoder passwordEncoder;
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

        if (changeAccessRequest.getOperation() == Operation.LOCK) {
            boolean userIsAdmin = user.getRole().equals(Role.ADMIN);
            if (userIsAdmin) {
                ErrorResponse response = new ErrorResponse("You can't lock admin");
                return ResponseEntity.badRequest().body(response);
            }

            user.setLocked(true);
        } else {
            user.setLocked(false);
        }

        userRepository.save(user);
        SuccessResponse successResponse = new SuccessResponse(String.format("User %sed successfully", user.getEmail(), operation.name().toLowerCase()));
        return ResponseEntity.ok(successResponse);
    }

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
        admin = adminRepository.save(admin);

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

        deliveryPartnerRepository.save(deliveryPartner);
        SuccessResponse response = new SuccessResponse("Delivery partner registered successfully");
        return ResponseEntity.ok(response);
    }



    private User getUserById(long userId) {
        // TODO: NOT FOUND EXCEPTION
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByEmail(String username) {
        return userRepository.existsByEmailIgnoreCase(username);
    }
}
