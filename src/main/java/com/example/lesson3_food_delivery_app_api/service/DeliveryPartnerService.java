package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.DeliveryPartnerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.DeliveryPartner;
import com.example.lesson3_food_delivery_app_api.entity.Order;
import com.example.lesson3_food_delivery_app_api.exception.OrderDeliveringException;
import com.example.lesson3_food_delivery_app_api.repository.DeliveryPartnerRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;

    private final PasswordEncoder passwordEncoder;

    private final OrderService orderService;

    public ResponseEntity<?> register(DeliveryPartnerRegistrationRequest registrationRequest) {
        String name = registrationRequest.getName();
        String email = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();

        if (deliveryPartnerRepository.existsByEmailIgnoreCase(email)) {
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

    public List<Order> viewReadyOrders() {
        // return orders which are ready to be delivered
        List<Order> readyOrders = orderService.getReadyOrders();
        return readyOrders;
    }

    public ResponseEntity<?> deliverOrder(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new OrderDeliveringException("Order is already delivered");
        }

        if (order.getStatus() == Order.OrderStatus.DELIVERING) {
            throw new OrderDeliveringException("Order is already being delivered");
        }

        order.setStatus(Order.OrderStatus.DELIVERING);
        orderService.saveOrder(order);

        return ResponseEntity.ok(new SuccessResponse("Order delivered successfully"));
    }

    public ResponseEntity<?> finishDelivery(Long orderId) {

        Order order = orderService.getOrderById(orderId);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new OrderDeliveringException("Order is already delivered");
        }

        if (order.getStatus() == Order.OrderStatus.READY) {
            throw new OrderDeliveringException("Order is not being delivered");
        }

        order.setStatus(Order.OrderStatus.DELIVERED);
        orderService.saveOrder(order);

        return ResponseEntity.ok(new SuccessResponse("Order delivered successfully"));
    }
}
