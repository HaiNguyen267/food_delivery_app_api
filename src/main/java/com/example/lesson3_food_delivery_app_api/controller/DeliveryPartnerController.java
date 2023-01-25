package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.DeliveryPartnerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.DeliveryPartner;
import com.example.lesson3_food_delivery_app_api.entity.Order;
import com.example.lesson3_food_delivery_app_api.service.DeliveryPartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/delivery-partner")
@AllArgsConstructor
public class DeliveryPartnerController {


    private final DeliveryPartnerService deliveryPartnerService;

    @Operation(summary = "Register a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurant registered successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                                    {
                                                        "status": 200,
                                                        "message": "Delivery partner registered successfully",
                                                        "data": {
                                                            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRlbDNAZC5jb20iLCJyb2xlIjoiREVMSVZFUllfUEFSVE5FUiIsImlhdCI6MTY3NDU2NzI5MSwiZXhwIjoxNjc1MTcyMDkxfQ.ZckzvLUpDrs5KzG81P1RX53ou3HYzDTAOido2lR43CY"
                                                        }
                                                    }
                                            """)

                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email was already taken",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Email was already taken"
                                            }
                                            """)
                            )
                    }
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeliveryPartnerRegistrationRequest deliveryPartnerRegistrationRequest) {
        return deliveryPartnerService.register(deliveryPartnerRegistrationRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "View all ready orders  successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Ready orders retrieved successfully",
                                                "data": [
                                                    {
                                                        "id": 3,
                                                        "quantity": 5,
                                                        "price": 25.0,
                                                        "orderTime": "2023-01-24 20:20",
                                                        "deliveryTime": null,
                                                        "status": "READY",
                                                        "restaurantName": "Two Bears Restaurant",
                                                        "foodName": "Egg fried rice",
                                                        "deliveryPartnerName": null
                                                    }
                                                ]
                                            }
                                            """
                                    )
                            )
                    }
            )
    })
    @GetMapping("/viewReadyOrders")
    public ResponseEntity<?> viewReadyOrders() {
        return deliveryPartnerService.viewReadyOrders();
    }



    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order delivery started successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                  "status": 200,
                                                  "message": "Order delivery started successfully",
                                                  "data": null
                                              }
                                            """
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order is already being delivering",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Order is already being delivering"
                                            }
                                            """
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Order not found"
                                            }
                                            """
                                    )
                            )
                    }
            )
    })
    @PostMapping("/deliverOrder/{orderId}")
    public ResponseEntity<?> deliverOrder(@PathVariable Long orderId) {
        String currentDeliveryPartnerEmail = getCurrentDeliveryPartner().getUsername();
        return deliveryPartnerService.deliverOrder(currentDeliveryPartnerEmail, orderId);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get delivering orders successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Delivering orders retrieved successfully",
                                                "data": [
                                                    {
                                                        "id": 4,
                                                        "quantity": 5,
                                                        "price": 25.0,
                                                        "orderTime": "2023-01-24 20:50",
                                                        "deliveryTime": null,
                                                        "status": "DELIVERING",
                                                        "foodName": "Egg fried rice",
                                                        "restaurantName": "Two Bears Restaurant",
                                                        "deliveryPartnerName": "Quick Move Company"
                                                    }
                                                ]
                                            }
                                            """)

                            )
                    }
            )

    })
    @GetMapping("/viewDeliveringOrders")
    public ResponseEntity<?> getDeliveringOrders() {
        String currentDeliveryPartnerEmail = getCurrentDeliveryPartner().getUsername();
        return deliveryPartnerService.getDeliveringOrders(currentDeliveryPartnerEmail);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order delivery is successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Order is delivered successfully",
                                                "data": null
                                            }
                                            """
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order delivery is already finished",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Order delivery is already finished"
                                            }
                                            """
                                )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order delivery is not started yet",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Order delivery is not started yet"
                                            }
                                            """
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Order not found"
                                            }
                                            """
                                    )
                            )
                    }
            )

    })
    @PostMapping("/finishDelivery/{orderId}")
    public ResponseEntity<?> finishDelivery(@PathVariable Long orderId) {
        String currentDeliveryPartnerEmail = getCurrentDeliveryPartner().getUsername();
        return deliveryPartnerService.finishDelivery(currentDeliveryPartnerEmail, orderId);
    }

    private UserDetails getCurrentDeliveryPartner() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
