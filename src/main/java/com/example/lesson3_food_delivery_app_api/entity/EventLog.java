package com.example.lesson3_food_delivery_app_api.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {

    public static enum Event{
        REGISTER,
        LOGIN,
        LOGIN_FAILED,
        // restaurant
        EDIT_MENU,

        // customer
        ORDER_FOOD,

        // delivery partner
        DELIVERY_ORDER,
        FINISH_DELIVERY,

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Event event;

    private LocalDateTime time;

    @OneToOne
    private User user;
}
