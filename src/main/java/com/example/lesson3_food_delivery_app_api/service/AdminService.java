package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.AdminRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.Admin;
import com.example.lesson3_food_delivery_app_api.repository.AdminRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final DeliveryPartnerService deliveryPartnerService;
    private final EventLogService eventLogService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(AdminRegistrationRequest adminRegistrationRequest) {
        return userService.registerAdmin(adminRegistrationRequest);

    }

    public List<?> getAllRestaurants() {
        return restaurantService.getAllRestaurants();

    }

    public List<?> getRestaurantOrders(long restaurantId) {
        return restaurantService.getRestaurantOrders(restaurantId);

    }

    public List<?> getAllCustomers() {
        return customerService.getAllCustomers();

    }

    public List<?> findAllDelieryOrderOfCustomer(long customerId) {
        return customerService.findAllUnDeliveredOrders(customerId);
    }

    public List<?> getEventLogs(Long userId) {
        return eventLogService.getEventLogs(userId);

    }

    public ResponseEntity<?> changeAccess(ChangeAccessRequest changeAccessRequest) {
        return userService.changeAccess(changeAccessRequest);
    }

    public List<?> getAllDeliveryPartners() {
        return deliveryPartnerService.getAllDeliveryPartners();

    }

    public List<?> getAllDeliveryOrder(long deliveryPartnerId) {
        return deliveryPartnerService.getAllDeliveryOrder(deliveryPartnerId);

    }


}
