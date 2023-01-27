package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.*;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.service.CustomerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    // TODO: swagger 3 for admin, customer, delivery partne, restaurant for the change of food order
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer registered successfully",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
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
                                                "id": 19,
                                                "name": "Pho",
                                                "price": 12.0,
                                                "description": "Vietnamese famous food",
                                                "imageUrl": null,
                                                "rating": null,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
                                            },
                                            {
                                                "id": 20,
                                                "name": "Band mi",
                                                "price": 2.0,
                                                "description": "A kind of sandwich",
                                                "imageUrl": null,
                                                "rating": null,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
                                            },
                                            {
                                                "id": 21,
                                                "name": "Egg fried rice",
                                                "price": 5.0,
                                                "description": "Fried rice with eggs",
                                                "imageUrl": null,
                                                "rating": 5.0,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
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
                                                "rating": null,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
                                            },
                                            {
                                                "id": 20,
                                                "name": "Band mi",
                                                "price": 2.0,
                                                "description": "A kind of sandwich",
                                                "imageUrl": null,
                                                "rating": null,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
                                            },
                                            {
                                                "id": 21,
                                                "name": "Egg fried rice",
                                                "price": 5.0,
                                                "description": "Fried rice with eggs",
                                                "imageUrl": null,
                                                "rating": 5.0,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
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
                                        "data": [
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
                    responseCode = "400",
                    description = "Restaurant id not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 404,
                                        "message": "Food not found for id: 52"
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

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Search food successfully",
                                        "data": [
                                            {
                                                "id": 22,
                                                "name": "Noodle",
                                                "price": 15.0,
                                                "description": "Spicy",
                                                "imageUrl": null,
                                                "rating": null,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
                                            },
                                            {
                                                "id": 23,
                                                "name": "Ramen Noodles",
                                                "price": 25.0,
                                                "description": "Japanese noodle",
                                                "imageUrl": null,
                                                "rating": null,
                                                "restaurantId": 2,
                                                "numberOfComments": 0
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No results",
                    content = @Content(
                            mediaType = "application/json",
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
    @GetMapping("/searchFood")
    public ResponseEntity<?> searchFood(@RequestParam String name) {
        return customerService.searchFood(name);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search restaurant successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Search restaurant successfully",
                                        "data": [
                                            {
                                                "id": 2,
                                                "name": "Two Bears Restaurant",
                                                "address": "Hanoi",
                                                "phone": "+8448291291"
                                            },
                                            {
                                                "id": 3,
                                                "name": "Two Princes Restaurant",
                                                "address": "Ho Chi Minh City",
                                                "phone": "+8447195721"
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No results",
                    content = @Content(
                            mediaType = "application/json",
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
    @GetMapping("/searchRestaurant")
    public ResponseEntity<?> searchRestaurant(@RequestParam String name) {
        return customerService.searchRestaurant(name);
    }

}
