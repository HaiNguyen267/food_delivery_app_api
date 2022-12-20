package com.example.lesson3_food_delivery_app_api.entity;

import com.example.lesson3_food_delivery_app_api.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    //TODO: log events
    //TODO: brute force protection

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    protected Role role;

    @JsonIgnore
    private long accessFailedCount; // 0 - 5, lock user after 5 failed attempts
    @JsonIgnore
    private boolean isLocked;

    @JsonIgnore
    @OneToMany(cascade =  {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
    private List<EventLog> eventLogs;
}
