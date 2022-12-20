package com.example.lesson3_food_delivery_app_api.repository;

import com.example.lesson3_food_delivery_app_api.entity.DeliveryPartner;
import com.example.lesson3_food_delivery_app_api.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<DeliveryPartner> findByEmailIgnoreCase(String currentDeliveryPartnerEmail);

    @Query("SELECT o FROM Order o WHERE o.deliveryPartner.id = :deliveryPartnerId AND o.status IN :orderStatuses")
    List<?> findAllOrdersByStatus(long deliveryPartnerId, EnumSet<OrderStatus> statuses);
}
