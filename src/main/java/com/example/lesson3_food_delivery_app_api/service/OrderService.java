package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.Order;
import com.example.lesson3_food_delivery_app_api.exception.NotFoundException;
import com.example.lesson3_food_delivery_app_api.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public List<Order> getReadyOrders() {
        List<Order> readyOrders = orderRepository.findReadyOrders(); // find all orders which are ready to be delivered
        return readyOrders;
    }

    public List<?> findUnDeliveredOrdersOfCustomer(String currentCustomerEmail) {
        return orderRepository.findUnDeliveredOrdersOfCustomer(currentCustomerEmail);
    }
}
