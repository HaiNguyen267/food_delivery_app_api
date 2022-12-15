package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.*;
import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.entity.Menu;
import com.example.lesson3_food_delivery_app_api.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/restaurant")
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
                            schema = @Schema(implementation = RegisterResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email was already registered",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatusException.class))
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
                                    schema = @Schema(implementation = SuccessResponse.class)
                            ),

                    }
            )
    })
    @PostMapping("/addMenu")
    public List<Menu> addMenu(@RequestBody Menu menu) {
        String email = getCurrentUser().getUsername();
        return restaurantService.addMenu(email, menu);
    }

    private UserDetails getCurrentUser() {
        UserDetails userdetails
                = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userdetails;
    }

//
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Menu edited successfully",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = SuccessResponse.class))
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Food not found",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ErrorResponse.class))
//                    }
//            )
//    })
//    @PatchMapping("/editMenu/{foodId}")
//    public ResponseEntity<?> editMenu(@PathVariable Long foodId, @RequestBody Food food) {
//        return restaurantService.editMenu(foodId, food);
//    }
//
//
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Delete food successfully",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = SuccessResponse.class))
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Food not found",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ErrorResponse.class))
//                    }
//            )
//    })
//    @DeleteMapping("/deleteFood/{foodId}")
//    public ResponseEntity<?> deleteFood(@PathVariable Long foodId) {
//        return restaurantService.deleteFood(foodId);
//    }
//
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Added food successfully",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = SuccessResponse.class))
//                    }
//            )
//    })
//    @PostMapping("/addFood")
//    public ResponseEntity<?> addFood(@RequestBody Food food) {
//        return restaurantService.addFood(food);
//    }
//
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Get orders successfully",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    examples = @ExampleObject(
//                                            name = "SuccessResponse",
//                                            value = "Get orders successfully"),
//                                    schema = @Schema(implementation = List<Order>.class))
//                    }
//            )
//    })
//    @GetMapping("/orders")
//    public ResponseEntity<?> getOrders() {
//        return restaurantService.getOrders();
//    }
//
//
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Track order successfully",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = Delivery.class))
//                    }
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Order not found",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ErrorResponse.class))
//                    }
//            )
//    })
//    @GetMapping("/trackOrderDelivery/{orderId}")
//    public ResponseEntity<?> trackOrderDelivery(@PathVariable Long orderId) {
//        return restaurantService.trackOrder(orderId);
//    }
}
