package com.example.lesson3_food_delivery_app_api.repository;

import com.example.lesson3_food_delivery_app_api.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    @Query("SELECT e FROM EventLog e WHERE e.user.id = :userId")
    List<?> findEventLogsByUserId(Long userId);
}
