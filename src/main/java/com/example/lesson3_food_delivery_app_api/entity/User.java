package com.example.lesson3_food_delivery_app_api.entity;

import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String email;
    protected String password;
    @Enumerated(EnumType.STRING)
    protected Role role;

}
