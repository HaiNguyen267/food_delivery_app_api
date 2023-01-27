package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.DeliveryPartnerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.FoodOrderDTO;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.DeliveryPartner;
import com.example.lesson3_food_delivery_app_api.entity.EventLog;
import com.example.lesson3_food_delivery_app_api.entity.FoodOrder;
import com.example.lesson3_food_delivery_app_api.entity.OrderStatus;
import com.example.lesson3_food_delivery_app_api.exception.NotFoundException;
import com.example.lesson3_food_delivery_app_api.exception.OrderDeliveringException;
import com.example.lesson3_food_delivery_app_api.repository.DeliveryPartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final FoodOrderService orderService;
    private final UserService userService;
    private final EventLogService eventLogService;

    public ResponseEntity<?> register(DeliveryPartnerRegistrationRequest deliveryPartnerRegistrationRequest) {
       return userService.registerDeliveryPartner(deliveryPartnerRegistrationRequest);
    }

    public ResponseEntity<?> viewReadyOrders() {
        // return orders which are ready to be delivered
        List<FoodOrder> readyOrders = orderService.getReadyOrders();

        List<FoodOrderDTO> readyOrderDTOs = readyOrders.stream()
                .map(FoodOrderDTO::new)
                .toList();

        SuccessResponse successResponse = new SuccessResponse(200, "Ready orders retrieved successfully", readyOrderDTOs);
        return ResponseEntity.ok(successResponse);
    }

    @Transactional
    public ResponseEntity<?> deliverOrder(String currentDeliveryPartnerEmail, Long orderId) {
        DeliveryPartner deliveryPartner = getDeliveryPartnerByEmail(currentDeliveryPartnerEmail);
        FoodOrder order = orderService.getOrderById(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderDeliveringException("Order is already delivered");
        }

        if (order.getStatus() == OrderStatus.DELIVERING) {
            throw new OrderDeliveringException("Order is already being delivering");
        }

        order.setStatus(OrderStatus.DELIVERING);
        order.setDeliveryPartner(deliveryPartner);
        orderService.saveOrder(order);

        deliveryPartner.getDeliveringOrders().add(order);
        deliveryPartner = deliveryPartnerRepository.save(deliveryPartner);
        eventLogService.saveEventLog(EventLog.Event.DELIVER_ORDER, deliveryPartner.getId());
        return ResponseEntity.ok(new SuccessResponse(200, "Order delivery started successfully"));
    }

    @Transactional
    public ResponseEntity<?> finishDelivery(String currentDeliveryPartnerEmail, Long orderId) {
        DeliveryPartner deliveryPartner = getDeliveryPartnerByEmail(currentDeliveryPartnerEmail);
        FoodOrder order = orderService.getOrderById(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderDeliveringException("Order delivery is already finished");
        }

        if (order.getStatus() == OrderStatus.READY) {
            throw new OrderDeliveringException("Order delivery is not started yet");
        }

        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveryTime(LocalDateTime.now());
        orderService.saveOrder(order);

        deliveryPartner.getDeliveringOrders().remove(order);
        deliveryPartner.getDeliveredOrders().add(order);
        deliveryPartner = deliveryPartnerRepository.save(deliveryPartner);

        eventLogService.saveEventLog(EventLog.Event.FINISH_DELIVERY, deliveryPartner.getId());
        return ResponseEntity.ok(new SuccessResponse(200,"Order is delivered successfully"));
    }

    private DeliveryPartner getDeliveryPartnerByEmail(String currentDeliveryPartnerEmail) {
        return deliveryPartnerRepository.findByEmailIgnoreCase(currentDeliveryPartnerEmail)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found"));
    }

    public List<DeliveryPartner> getAllDeliveryPartners() {
        return deliveryPartnerRepository.findAll();
    }

    public List<FoodOrder> getAllDeliveryOrder(long deliveryPartnerId) {
         DeliveryPartner deliveryPartner = deliveryPartnerRepository.findById(deliveryPartnerId)
                 .orElseThrow(() -> new NotFoundException("Delivery partner not found"));

         // get order deliveries of the delivery partner, both delivered and delivering
         List<FoodOrder> allOrderDeliveries = deliveryPartner.getDeliveringOrders();
         allOrderDeliveries.addAll(deliveryPartner.getDeliveredOrders());

         return allOrderDeliveries;
    }

    public ResponseEntity<?> getDeliveringOrders(String currentDeliveryPartnerEmail) {
        DeliveryPartner deliveryPartner = getDeliveryPartnerByEmail(currentDeliveryPartnerEmail);
        List<FoodOrder> deliveringOrders = deliveryPartner.getDeliveringOrders();

        List<FoodOrderDTO> deliveringOrderDTOs = deliveringOrders.stream()
                .map(FoodOrderDTO::new)
                .toList();
        SuccessResponse response = new SuccessResponse(200, "Delivering orders retrieved successfully", deliveringOrderDTOs);
        return ResponseEntity.ok(response);
    }

}
