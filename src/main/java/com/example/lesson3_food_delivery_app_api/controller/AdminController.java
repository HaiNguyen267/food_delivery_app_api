package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.ChangeAccessRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.AdminRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.Customer;
import com.example.lesson3_food_delivery_app_api.service.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Admin registered successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Admin registered successfully",
                                                "data": {
                                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJlbWFpbCI6ImFkbTJAZC5jb20iLCJpYXQiOjE2NzQ1Njg3OTEsImV4cCI6MTY3NTE3MzU5MX0.tS9GTrSSb8n6JKwSHj5l8YDMJ8f8qerM_bO6XgeS1BM"
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                          "status": 200,
                                          "message": "Event logs retrieved successfully",
                                          "data": [
                                              {
                                                  "id": 1,
                                                  "event": "REGISTER",
                                                  "time": "2023-01-24 12:22:38"
                                              },
                                              {
                                                  "id": 2,
                                                  "event": "LOGIN",
                                                  "time": "2023-01-24 12:23:16"
                                              },
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                          "status": 404,
                                          "message": "User not found"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/viewAllLogs/{userId}")
    public ResponseEntity<?> getEventLogs(@PathVariable Long userId) {
        return adminService.getEventLogs(userId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Change access successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                           "status": 200,
                                           "message": "User cus2@c.com locked successfully",
                                           "data": null
                                       }
                                    """)
                    )

            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot change access of the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "message": "User is already locked"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "User not found"
                                    }
                                    """)
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
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "All restaurants retrieved successfully",
                                        "data": [
                                            {
                                                "id": 2,
                                                "name": "Two Bears Restaurant",
                                                "address": "Hanoi",
                                                "phone": "+8448291291"
                                            },
                                            {
                                                "id": 5,
                                                "name": "The Coconut Restaurant",
                                                "address": "Hanoi",
                                                "phone": "+8448211291"
                                            }                       
                                        ]
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/getAllRestaurants")
    public ResponseEntity<?> getAllRestaurants() {
        return adminService.getAllRestaurants();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all orders of a restaurant successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Restaurant orders retrieved successfully",
                                        "data": [
                                            {
                                                "orderId": 8,
                                                "foodItems": [
                                                    {
                                                        "foodId": 1,
                                                        "foodName": "Pho",
                                                        "price": 15.0,
                                                        "quantity": 3
                                                    }
                                                ],
                                                "restaurantId": 1,
                                                "restaurantName": "Restaurant A",
                                                "deliveryPartnerId": 13,
                                                "totalPrice": 45.0
                                            },
                                            {
                                                "orderId": 10,
                                                "foodItems": [
                                                    {
                                                        "foodId": 5,
                                                        "foodName": "Extra Tips",
                                                        "price": 10.5,
                                                        "quantity": 3
                                                    }
                                                ],
                                                "restaurantId": 1,
                                                "restaurantName": "Restaurant A",
                                                "deliveryPartnerId": null,
                                                "totalPrice": 31.5
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Restaurant not found"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/getRestaurantOrders/{restaurantId}")
    public ResponseEntity<?> getAllRestaurantOrder(@PathVariable long restaurantId) {
        return adminService.getRestaurantOrders(restaurantId);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all customer successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "All customers retrieved successfully",
                                        "data": [
                                            {
                                                "id": 1,
                                                "name": "Hai",
                                                "address": "Vietnam"
                                            },
                                            {
                                                "id": 8,
                                                "name": "Hoang",
                                                "address": "Vietnam"
                                            },
                                            {
                                                "id": 9,
                                                "name": "Hoa",
                                                "address": "Vietnam"
                                            }
                                        ]
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/getAllCustomers")
    public ResponseEntity<?> getAllCustomer() {
        return adminService.getAllCustomers();
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all orders of customer successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "All delivery orders of customer retrieved successfully",
                                        "data": [
                                            {
                                                "orderId": 9,
                                                "foodItems": [
                                                    {
                                                        "foodId": 10,
                                                        "foodName": "Sticky Rice",
                                                        "price": 22.0,
                                                        "quantity": 5
                                                    },
                                                    {
                                                        "foodId": 12,
                                                        "foodName": "Pho",
                                                        "price": 13.0,
                                                        "quantity": 1
                                                    }
                                                ],
                                                "restaurantId": 3,
                                                "restaurantName": "Restaurant C",
                                                "deliveryPartnerId": null,
                                                "totalPrice": 123.0
                                            },
                                            {
                                                "orderId": 10,
                                                "foodItems": [
                                                    {
                                                        "foodId": 5,
                                                        "foodName": "Extra Tips",
                                                        "price": 10.5,
                                                        "quantity": 3
                                                    }
                                                ],
                                                "restaurantId": 1,
                                                "restaurantName": "Restaurant A",
                                                "deliveryPartnerId": null,
                                                "totalPrice": 31.5
                                            },
                                            {
                                                "orderId": 11,
                                                "foodItems": [
                                                    {
                                                        "foodId": 10,
                                                        "foodName": "Sticky Rice",
                                                        "price": 22.0,
                                                        "quantity": 5
                                                    },
                                                    {
                                                        "foodId": 12,
                                                        "foodName": "Pho",
                                                        "price": 13.0,
                                                        "quantity": 1
                                                    }
                                                ],
                                                "restaurantId": 3,
                                                "restaurantName": "Restaurant C",
                                                "deliveryPartnerId": null,
                                                "totalPrice": 123.0
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Customer not found"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/getCustomerOrders/{customerId}")
    public ResponseEntity<?> getCustomerOrders(@PathVariable long customerId) {
        return adminService.findAllDelieryOrderOfCustomer(customerId);
    }



    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all delivery partners successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                         "status": 200,
                                         "message": "All delivery orders of delivery partner retrieved successfully",
                                         "data": [
                                             {
                                                 "orderId": 8,
                                                 "foodItems": [
                                                     {
                                                         "foodId": 1,
                                                         "foodName": "Pho",
                                                         "price": 15.0,
                                                         "quantity": 3
                                                     }
                                                 ],
                                                 "restaurantId": 1,
                                                 "restaurantName": "Restaurant A",
                                                 "deliveryPartnerId": 13,
                                                 "totalPrice": 45.0
                                             }
                                         ]
                                     }
                                    """)
                    )
            )

    })
    @GetMapping("/getAllDeliveryPartners")
    public ResponseEntity<?> getAllDeliveryPartners() {
        return adminService.getAllDeliveryPartners();
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All delivery orders of delivery partner retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "All delivery orders of delivery partner retrieved successfully",
                                        "data": [
                                            {
                                                "id": 1,
                                                "quantity": 5,
                                                "price": 75.0,
                                                "orderTime": "2023-01-24 12:28",
                                                "deliveryTime": "2023-01-24 12:32",
                                                "status": "DELIVERED",
                                                "foodName": "Pho",
                                                "deliveryPartnerName": "Express Company",
                                                "restaurantName": "Two Bears Restaurant"
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Delivery partner not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                          "status": 404,
                                          "message": "Delivery partner not found"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/getAllDeliveryOrder/{deliveryPartnerId}")
    public ResponseEntity<?> getAllDeliveryOrder(@PathVariable long deliveryPartnerId) {
        return adminService.getAllDeliveryOrder(deliveryPartnerId);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search user successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Search user successfully",
                                        "data": [
                                            {
                                                "id": 1,
                                                "email": "cus2@c.com",
                                                "role": "CUSTOMER",
                                                "locked": false
                                            },
                                            {
                                                "id": 8,
                                                "email": "cus3@c.com",
                                                "role": "CUSTOMER",
                                                "locked": false
                                            },
                                            {
                                                "id": 9,
                                                "email": "cus5@c.com",
                                                "role": "CUSTOMER",
                                                "locked": false
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No results",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                          "status": 404,
                                          "message": "No results"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/searchUser")
    public ResponseEntity<?> searchUser(@RequestParam String email) {
        return userService.searchByEmail(email);
    }

}
