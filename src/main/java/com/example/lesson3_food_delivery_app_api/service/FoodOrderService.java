package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.FoodOrder;
import com.example.lesson3_food_delivery_app_api.exception.NotFoundException;
import com.example.lesson3_food_delivery_app_api.repository.FoodOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;

    public void saveOrder(FoodOrder order) {
        foodOrderRepository.save(order);
    }

    public FoodOrder getOrderById(Long orderId) {
        return foodOrderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public List<FoodOrder> getReadyOrders() {
        List<FoodOrder> readyOrders = foodOrderRepository.findReadyOrders(); // find all orders which are ready to be delivered
        return readyOrders;
    }

    public List<FoodOrder> findUnDeliveredOrdersOfCustomer(String currentCustomerEmail) {
        return foodOrderRepository.findUnDeliveredOrdersOfCustomer(currentCustomerEmail);
    }
}
