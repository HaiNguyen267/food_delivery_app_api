package com.example.lesson3_food_delivery_app_api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLog {

    public static enum Event{
        REGISTER,
        LOGIN,
        LOGIN_FAILED,
        BRUTE_FORCE,

        // restaurant
        EDIT_MENU,

        // customer
        ORDER_FOOD,
        COMMENT_FOOD,
        RATE_FOOD,

        // delivery partner
        DELIVER_ORDER,
        FINISH_DELIVERY,

        // admin
        LOCK_USER,
        UNLOCK_USER

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Event event;

    private LocalDateTime time;

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @JsonIgnore
    @ManyToOne
    private User user;
}
