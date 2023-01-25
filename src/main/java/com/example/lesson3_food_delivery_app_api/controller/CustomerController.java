package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.*;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.service.CustomerService;
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

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {
    //TODO: swagger3 for this class
    private final CustomerService customerService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer registered successfully",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Customer registered successfully",
                                        "data": {
                                            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ1VTVE9NRVIiLCJlbWFpbCI6ImN1czNAYy5jb20iLCJpYXQiOjE2NzQ1NTgwMzksImV4cCI6MTY3NTE2MjgzOX0.AJmtO2jLNF1GahDPnbstJwVZCulIGI6_pB-ObbqhVMo"
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
    public ResponseEntity<?> register(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        return customerService.register(customerRegistrationRequest);
    }


    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "View all food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "All foods retrieved successfully",
                                        "data": [
                                            {
                                                "id": 1,
                                                "name": "Pho",
                                                "price": 15.0,
                                                "description": "Vietnamese famous food",
                                                "imageUrl": null,
                                                "rating": null
                                            },
                                            {
                                                "id": 2,
                                                "name": "Band mi",
                                                "price": 2.0,
                                                "description": "A kind of sandwich",
                                                "imageUrl": null,
                                                "rating": null
                                            },
                                            {
                                                "id": 3,
                                                "name": "Egg fried rice",
                                                "price": 5.0,
                                                "description": "Fried rice with eggs",
                                                "imageUrl": null,
                                                "rating": null
                                            }
                                        ]
                                    }
                                    """)
                    )
            )
    )
    @GetMapping("/viewAllFoods")
    public ResponseEntity<?>  viewAllFoods() {
        return customerService.viewAllFoods();
    }

    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "View all restaurants successfully",
                    content = @Content(
                            mediaType = "application/json",
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
    )
    @GetMapping("/viewAllRestaurants")
    public ResponseEntity<?>  viewAllRestaurants() {
        return customerService.viewAllRestaurants();
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "View all foods by restaurant id successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Restaurant menu retrieved successfully",
                                        "data": [
                                            {
                                                "id": 19,
                                                "name": "Pho",
                                                "price": 12.0,
                                                "description": "Vietnamese famous food",
                                                "imageUrl": null,
                                                "rating": null
                                            },
                                            {
                                                "id": 20,
                                                "name": "Band mi",
                                                "price": 2.0,
                                                "description": "A kind of sandwich",
                                                "imageUrl": null,
                                                "rating": null
                                            },
                                            {
                                                "id": 21,
                                                "name": "Egg fried rice",
                                                "price": 5.0,
                                                "description": "Fried rice with eggs",
                                                "imageUrl": null,
                                                "rating": 5.0
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Restaurant id not found",
                    content = @Content(
                            mediaType = "application/json",
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
    @GetMapping("/getRestaurantMenu/{restaurantId}")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable long restaurantId) {
        return customerService.getRestaurantMenu(restaurantId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer ordered food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Food ordered successfully",
                                        "data": {
                                            "foodId": 21,
                                            "restaurantName": "Two Bears Restaurant",
                                            "orderId": 3,
                                            "foodName": "Egg fried rice",
                                            "quantity": 5,
                                            "price": 25.0
                                        }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Restaurant id not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Food not found"
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/orderFood")
    public ResponseEntity<?> orderFood(@RequestBody OrderFoodRequest orderFoodRequest) {
        String currentCustomerEmail = getCurrentCustomer().getUsername();
        return customerService.orderFood(currentCustomerEmail, orderFoodRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "View food comments successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "View food comments successfully",
                                        "data": [
                                            {
                                                "id": 1,
                                                "content": "tasty"
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples =  @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Food not found"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/viewFoodComment/{foodId}")
    public ResponseEntity<?>  getFoodComments(@PathVariable Long foodId) {
        return customerService.getFoodComments(foodId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get orders successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Orders retrieved successfully",
                                        "data": [
                                            {
                                                "id": 2,
                                                "quantity": 5,
                                                "price": 25.0,
                                                "orderTime": "2023-01-24 18:26",
                                                "deliveryTime": null,
                                                "status": "READY",
                                                "deliveryPartnerName": null,
                                                "foodName": "Egg fried rice",
                                                "restaurantName": "Two Bears Restaurant"
                                            },
                                            {
                                                "id": 3,
                                                "quantity": 5,
                                                "price": 25.0,
                                                "orderTime": "2023-01-24 20:20",
                                                "deliveryTime": null,
                                                "status": "READY",
                                                "deliveryPartnerName": null,
                                                "foodName": "Egg fried rice",
                                                "restaurantName": "Two Bears Restaurant"
                                            }
                                        ]
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/myOrders")
    public ResponseEntity<?>  getOrders() {
        String currentCustomerEmail = getCurrentCustomer().getUsername();
        return customerService.getOrders(currentCustomerEmail);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer commented food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Commented successfully",
                                        "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Customer has not ordered this food yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "message": "You have to order this food before commenting"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Food not found"
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/commentFood")
    public ResponseEntity<?> commentFood(@RequestBody FoodCommentRequest foodCommentRequest) {
        String currentCustomerEmail = getCurrentCustomer().getUsername();
        return customerService.commentFood(currentCustomerEmail, foodCommentRequest);
    }



    private UserDetails getCurrentCustomer() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer commented food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Rated successfully",
                                        "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Customer has not ordered this food yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "message": "You have to order this food before rating"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Food not found"
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/rateFood")
    public ResponseEntity<?> rateFood(@RequestBody FoodRatingRequest foodRatingRequest) {
        String currentCustomerEmail = getCurrentCustomer().getUsername();
        return customerService.rateFood(currentCustomerEmail, foodRatingRequest);
    }
}
