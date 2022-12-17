package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.CustomerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.FoodCommentRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.FoodRatingRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.exception.RatingInvalidException;
import com.example.lesson3_food_delivery_app_api.exception.UserHasNotOrderedFoodException;
import com.example.lesson3_food_delivery_app_api.repository.CustomerRepository;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FoodService foodService;

    @Autowired
    private RestaurantService restaurantService;

    public ResponseEntity<?> register(CustomerRegistrationRequest customerRegistrationRequest) {
        String name = customerRegistrationRequest.getName();
        String email = customerRegistrationRequest.getEmail();
        String password = customerRegistrationRequest.getPassword();
        String address = customerRegistrationRequest.getAddress();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            ErrorResponse response = new ErrorResponse("Email already registered");
            return ResponseEntity.badRequest().body(response);
        }

        Customer customer = Customer.builder()
                .name(name)
                .address(address)
                .build();

        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setRole(Role.CUSTOMER);

        customer = customerRepository.save(customer);

        return AuthService.createResponseWithAccessToken(customer);

    }

    public List<Food> viewAllFoods() {
        return foodService.getAllFoods();
    }

    public List<Restaurant> viewAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    public ResponseEntity<?> getRestaurantMenu(long restaurantId) {
        return restaurantService.getRestaurantMenu(restaurantId);
    }

    public ResponseEntity<?> commentFood(String userEmail, FoodCommentRequest foodCommentRequest) {
        Food food = foodService.getFood(foodCommentRequest.getFoodId());

        Customer customer = getCustomerByEmail(userEmail);
        // to be able to comment, the customer must have ordered the food
        if (!checkIfUserHasOrderedFood(customer, food)) {
            throw new UserHasNotOrderedFoodException("You have to order this food before commenting");
        }

        Comment comment = Comment.builder()
                .content(foodCommentRequest.getComment())
                .user(customer)
                .build();


        food.getComments().add(comment);
        foodService.saveFood(food);

        SuccessResponse response = new SuccessResponse("Commented successfully");
        return ResponseEntity.ok(response);
    }

    private Customer getCustomerByEmail(String userEmail) {
        return customerRepository.findByEmail(userEmail);
    }



    public ResponseEntity<?> rateFood(String currentCustomerEmail, FoodRatingRequest foodRatingRequest) {
        Food food = foodService.getFood(foodRatingRequest.getFoodId());

        Customer customer = getCustomerByEmail(currentCustomerEmail);
        // to be able to rate, the customer must have ordered the food
        if (!checkIfUserHasOrderedFood(customer, food)) {
            throw new UserHasNotOrderedFoodException("You have to order this food before rating");
        }

        if (isRatingValid(foodRatingRequest.getRating())) {
            throw new RatingInvalidException("Rating must be between 1 and 5");
        }

        Rating rating = Rating.builder()
                .rating(foodRatingRequest.getRating())
                .build();

        food.getRatings().add(rating);
        foodService.saveFood(food);


        SuccessResponse response = new SuccessResponse("Rated successfully");
        return ResponseEntity.ok(response);
    }

    private boolean isRatingValid(Integer rating) {
        return rating >= 1 && rating <= 5;
    }

    private boolean checkIfUserHasOrderedFood(Customer customer, Food food) {
        return customer.getOrders().stream().anyMatch(order -> order.getFoodId().equals(food.getId()));
    }
}
