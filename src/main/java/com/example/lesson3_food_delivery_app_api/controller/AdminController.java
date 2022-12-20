package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.ChangeAccessRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.AdminRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.CustomerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.service.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    // this class


    private final AdminService adminService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Admin registered successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email was already taken",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminRegistrationRequest adminRegistrationRequest) {
        return adminService.register(adminRegistrationRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all event logs of a user successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EventLog.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/viewAllLogs/{userId}")
    public List<?> getEventLogs(@PathVariable Long userId) {
        return adminService.getEventLogs(userId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Change access successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = SuccessResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot change access of the user",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            )
    })
    @PostMapping("/changeAccess")
    public ResponseEntity<?> changeAccess(@RequestBody ChangeAccessRequest changeAccessRequest) {
        return adminService.changeAccess(changeAccessRequest);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all restaurants successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Restaurant.class)
                            )
                    )
            )
    })
    @GetMapping("/getAllRestaurants")
    public List<?> getAllRestaurants() {
        return adminService.getAllRestaurants();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all orders of a restaurant successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Order.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Restaurant not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    })
    @GetMapping("/getAllOrders/{restaurantId}")
    public List<?> getAllRestaurantOrder(@PathVariable long restaurantId) {
        return adminService.getRestaurantOrders(restaurantId);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all customer successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Customer.class)
                            )
                    )
            )
    })
    @GetMapping("/getAllCustomers")
    public List<?> getAllCustomer() {
        return adminService.getAllCustomers();
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all orders of customer successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Order.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/getCustomerOrders/{customerId}")
    public List<?> getCustomerOrders(@PathVariable long customerId) {
        return adminService.findAllDelieryOrderOfCustomer(customerId);
    }



    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all delivery partners successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = DeliveryPartner.class)
                            )
                    )
            )

    })
    @GetMapping("/getAllDeliveryPartners")
    public List<?> getAllDeliveryPartners() {
        return adminService.getAllDeliveryPartners();
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all orders of a delivery partner successfully",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Order.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Delivery partner not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/getAllDeliveryOrder/{deliveryPartnerId}")
    public List<?> getAllDeliveryOrder(@PathVariable long deliveryPartnerId) {
        return adminService.getAllDeliveryOrder(deliveryPartnerId);
    }
}
