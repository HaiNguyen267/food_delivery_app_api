package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.GetOrdersResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.entity.Menu;
import com.example.lesson3_food_delivery_app_api.entity.Order;
import com.example.lesson3_food_delivery_app_api.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

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
                                        "message": "Register successfully",
                                        "data": {
                                            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUkVTVEFVUkFOVCIsImVtYWlsIjoicmVzNEByLmNvbSIsImlhdCI6MTY3NDU1NzQ3NiwiZXhwIjoxNjc1MTYyMjc2fQ.kYpb3mb74okBViIa_nN0O5tdtMPGzx8otdmfIzo1hmA"
                                        }
                                    }
                                    """))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email was already registered",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "message": "Email already registered"
                                    }
                                    """))
                    }
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RestaurantRegistrationRequest registrationRequest) {
        return restaurantService.register(registrationRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Menu added successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Menu added successfully",
                                                "data": null
                                            }
                                            """)
                            )

                    }
            )
    })
    @PostMapping("/addMenu")
    public ResponseEntity<?> addMenu(@RequestBody Menu menu) {
        // get email of current logged in restaurant email
        String email = getCurrentRestaurant().getUsername();
        return restaurantService.addMenu(email, menu);
    }

    private UserDetails getCurrentRestaurant() {
        UserDetails userdetails
                = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userdetails;
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Menu edited successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Food edited successfully",
                                                "data": {
                                                    "id": 18,
                                                    "name": "Pho",
                                                    "price": 15.0,
                                                    "description": "Vietnamese famous food",
                                                    "imageUrl": null,
                                                    "rating": null
                                                }
                                            }
                                            """)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Food id does not exist in menu"
                                            }
                                            """)
                            )
                    }
            )
    })

    @PatchMapping("/editMenu/{foodId}")
    public ResponseEntity<?> editMenu(@PathVariable Long foodId, @RequestBody Food food) {
        String email = getCurrentRestaurant().getUsername();
        return restaurantService.editMenu(email, foodId, food);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Delete food successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Delete food successfully",
                                                "data": null
                                            }
                                            """)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Food id does not exist in menu"
                                            }
                                            """)
                            )
                    }
            )
    })
    @DeleteMapping("/deleteFood/{foodId}")
    public ResponseEntity<?> deleteFood(@PathVariable Long foodId) {
        String email = getCurrentRestaurant().getUsername();
        return restaurantService.deleteFood(email,foodId);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Added food successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class))
                    }
            )
    })
    @PostMapping("/addFood")
    public ResponseEntity<?> addFood(@RequestBody Food food) {
        String email = getCurrentRestaurant().getUsername();
        return restaurantService.addFood(email, food);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get orders successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GetOrdersResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Orders retrieved successfully",
                                                "data": [
                                                    {
                                                        "id": 1,
                                                        "price": 75.0,
                                                        "foodId": 1,
                                                        "deliveryPartnerId": 3,
                                                        "status": "DELIVERED"
                                                    }
                                                ]
                                            }
                                            """)
                            )
                    }
            )
    })
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        String email = getCurrentRestaurant().getUsername();
        return restaurantService.getOrders(email);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get menu successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Menu retrieved successfully",
                                                "data": [
                                                    {
                                                        "id": 13,
                                                        "name": "Band mi",
                                                        "price": 2.0,
                                                        "description": "A kind of sandwich",
                                                        "imageUrl": null,
                                                        "rating": null
                                                    },
                                                    {
                                                        "id": 14,
                                                        "name": "Pho",
                                                        "price": 15.0,
                                                        "description": "Vietnamese famous food",
                                                        "imageUrl": null,
                                                        "rating": null
                                                    },
                                                    {
                                                        "id": 15,
                                                        "name": "Salad",
                                                        "price": 5.0,
                                                        "description": "made from fresh vegetables",
                                                        "imageUrl": null,
                                                        "rating": null
                                                    }
                                                ]           
                                            }
                                            """
                                    )
                            )
                    }
            )
    })
    @GetMapping("/menu")
    public ResponseEntity<?> getMenu() {
        String email = getCurrentRestaurant().getUsername();
        return restaurantService.getMenu(email);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Track order successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                  "status": 200,
                                                  "message": "Order retrieved successfully",
                                                  "data": {
                                                      "id": 1,
                                                      "quantity": 5,
                                                      "price": 75.0,
                                                      "orderTime": "2023-01-24 12:28",
                                                      "deliveryTime": "2023-01-24 12:32",
                                                      "status": "DELIVERED",
                                                      "restaurantName": "Two Bears Restaurant",
                                                      "foodName": "Pho",
                                                      "deliveryPartnerName": "Express Company"
                                                  }
                                            }
                                            """)

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
                                                "message": "Order not found in order list"
                                            }
                                            """)
                            )
                    }
            )
    })
    @GetMapping("/trackOrderDelivery/{orderId}")
    public ResponseEntity<?> trackOrderDelivery(@PathVariable Long orderId) {
        String email = getCurrentRestaurant().getUsername();

        return restaurantService.trackOrder(email, orderId);
    }
}
