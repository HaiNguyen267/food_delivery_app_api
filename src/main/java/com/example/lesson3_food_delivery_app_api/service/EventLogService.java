package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.EventLog;
import com.example.lesson3_food_delivery_app_api.entity.User;
import com.example.lesson3_food_delivery_app_api.repository.EventLogRepository;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventLogService {

    private final EventLogRepository eventLogRepository;
    private final UserRepository userRepository;
    public List<?> getEventLogs(Long userId) {
        return eventLogRepository.findEventLogsByUserId(userId);
    }

    public void saveEventLog(EventLog.Event event, Long userId) {
        User user = userRepository.findById(userId).get();
        EventLog eventLog = EventLog.builder()
                .event(event)
                .user(user)
                .time(LocalDateTime.now())
                .build();
        eventLogRepository.save(eventLog);
    }
}
