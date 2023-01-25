package com.example.lesson3_food_delivery_app_api.repository;

import com.example.lesson3_food_delivery_app_api.entity.Customer;
import com.example.lesson3_food_delivery_app_api.entity.Order;
import com.example.lesson3_food_delivery_app_api.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String userEmail);


//    @Query("SELECT o FROM Order o where o.customer.id = ?1 AND o.status IN(com.example.lesson3_food_delivery_app_api.entity.OrderStatus.READY, com.example.lesson3_food_delivery_app_api.entity.OrderStatus.DELIVERING)") // JPQL, native SQl
    @Query("SELECT o FROM Order o where o.customer.id = :customerId AND o.status IN :statuses") // JPQL with params
    List<Order> findAllDeliveryByStatus(long customerId, EnumSet<OrderStatus> statuses);
}
