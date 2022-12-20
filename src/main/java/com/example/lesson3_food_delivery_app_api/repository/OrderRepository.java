package com.example.lesson3_food_delivery_app_api.repository;

import com.example.lesson3_food_delivery_app_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.status = 'READY'")
    List<Order> findReadyOrders();

    @Query("SELECT o FROM Order o WHERE o.customer.email = ?1 AND o.status <> 'DELIVERED'")
    List<Order> findUnDeliveredOrdersOfCustomer(String currentCustomerEmail);

}
