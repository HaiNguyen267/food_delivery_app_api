package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.*;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.Customer;
import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.entity.Restaurant;
import com.example.lesson3_food_delivery_app_api.service.CustomerService;
import io.swagger.v3.oas.annotations.media.Content;
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
    private final CustomerService customerService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer registered successfully",
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
    public ResponseEntity<?> register(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        return customerService.register(customerRegistrationRequest);
    }


    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "View all food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            )
    )
    @GetMapping("/viewAllFoods")
    public List<Food> viewAllFoods() {
        return customerService.viewAllFoods();
    }

    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "View all restaurants successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            )
    )
    @GetMapping("/viewAllRestaurants")
    public List<Restaurant> viewAllRestaurants() {
        return customerService.viewAllRestaurants();
    }



    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "View all foods by restaurant id successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Restaurant id not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Customer ordered food succesfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Restaurant id not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/viewFoodComment/{foodId}")
    public List<?> getFoodComments(@PathVariable Long foodId) {
        return customerService.getFoodComments(foodId);
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer commented food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Customer has not ordered this food yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Customer rated food successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Customer has not ordered this food yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/rateFood")
    public ResponseEntity<?> rateFood(@RequestBody FoodRatingRequest foodRatingRequest) {
        String currentCustomerEmail = getCurrentCustomer().getUsername();
        return customerService.rateFood(currentCustomerEmail, foodRatingRequest);
    }
}
