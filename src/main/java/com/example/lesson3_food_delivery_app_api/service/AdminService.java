package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.AdminRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
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
    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final DeliveryPartnerService deliveryPartnerService;
    private final EventLogService eventLogService;
    private final UserService userService;

    public ResponseEntity<?> register(AdminRegistrationRequest adminRegistrationRequest) {
        return userService.registerAdmin(adminRegistrationRequest);

    }

    public ResponseEntity<?> getAllRestaurants() {
        List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
        SuccessResponse successResponse = new SuccessResponse(200, "All restaurants retrieved successfully", allRestaurants);
        return ResponseEntity.ok(successResponse);

    }

    public ResponseEntity<?> getRestaurantOrders(long restaurantId) {
        List<Order> restaurantOrders = restaurantService.getRestaurantOrders(restaurantId);
        SuccessResponse successResponse = new SuccessResponse(200, "Restaurant orders retrieved successfully", restaurantOrders);
        return ResponseEntity.ok(successResponse);

    }

    public ResponseEntity<?> getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        SuccessResponse successResponse = new SuccessResponse(200, "All customers retrieved successfully", allCustomers);
        return ResponseEntity.ok(successResponse);
    }

    public ResponseEntity<?> findAllDelieryOrderOfCustomer(long customerId) {
        List<Order> allUnDeliveredOrders = customerService.findAllUnDeliveredOrders(customerId);
        SuccessResponse successResponse = new SuccessResponse(200, "All delivery orders of customer retrieved successfully", allUnDeliveredOrders);
        return ResponseEntity.ok(successResponse);
    }

    public ResponseEntity<?> getEventLogs(Long userId) {
        List<EventLog> eventLogs = eventLogService.getEventLogs(userId);
        SuccessResponse successResponse = new SuccessResponse(200, "Event logs retrieved successfully", eventLogs);
        return ResponseEntity.ok(successResponse);
    }

    public ResponseEntity<?> changeAccess(ChangeAccessRequest changeAccessRequest) {
         return userService.changeAccess(changeAccessRequest);
    }

    public ResponseEntity<?> getAllDeliveryPartners() {
        List<DeliveryPartner> allDeliveryPartners = deliveryPartnerService.getAllDeliveryPartners();
        SuccessResponse successResponse = new SuccessResponse(200, "All delivery partners retrieved successfully", allDeliveryPartners);
        return ResponseEntity.ok(successResponse);
    }

    public ResponseEntity<?> getAllDeliveryOrder(long deliveryPartnerId) {
        List<Order> allDeliveryOrder = deliveryPartnerService.getAllDeliveryOrder(deliveryPartnerId);
        SuccessResponse successResponse = new SuccessResponse(200, "All delivery orders of delivery partner retrieved successfully", allDeliveryOrder);
        return ResponseEntity.ok(successResponse);
    }


}
