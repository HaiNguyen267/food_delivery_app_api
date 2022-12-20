package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.repository.EventLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventLogService {

    private final EventLogRepository eventLogRepository;

    public List<?> getEventLogs(Long userId) {
        return eventLogRepository.findEventLogsByUserId(userId);
    }
}
