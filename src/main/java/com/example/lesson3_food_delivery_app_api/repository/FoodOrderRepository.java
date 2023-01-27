package com.example.lesson3_food_delivery_app_api.repository;

import com.example.lesson3_food_delivery_app_api.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    @Query("SELECT o FROM FoodOrder o WHERE o.status = 'READY'")
    List<FoodOrder> findReadyOrders();

    @Query("SELECT o FROM FoodOrder o WHERE o.customer.email = ?1 AND o.status <> 'DELIVERED'")
    List<FoodOrder> findUnDeliveredOrdersOfCustomer(String currentCustomerEmail);

}
