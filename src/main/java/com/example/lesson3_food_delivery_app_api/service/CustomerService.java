package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.CustomerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.FoodCommentRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.FoodRatingRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.OrderFoodRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.OrderFoodResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.exception.RatingInvalidException;
import com.example.lesson3_food_delivery_app_api.exception.UserHasNotOrderedFoodException;
import com.example.lesson3_food_delivery_app_api.repository.CustomerRepository;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final FoodService foodService;

    private final RestaurantService restaurantService;

    private final OrderService orderService;

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
        Food food = foodService.getFoodById(foodCommentRequest.getFoodId());

        Customer customer = getCustomerByEmail(userEmail);
        // to be able to comment, the customer must have ordered the food
        if (!checkIfUserHasOrderedFood(customer, food)) {
            throw new UserHasNotOrderedFoodException("You have to order this food before commenting");
        }

        Comment comment = Comment.builder()
                .content(foodCommentRequest.getComment())
                .customer(customer)
                .food(food)
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
        Food food = foodService.getFoodById(foodRatingRequest.getFoodId());

        Customer customer = getCustomerByEmail(currentCustomerEmail);
        // to be able to rate, the customer must have ordered the food
        if (!checkIfUserHasOrderedFood(customer, food)) {
            throw new UserHasNotOrderedFoodException("You have to order this food before rating");
        }

        if (!isRatingValid(foodRatingRequest.getRating())) {
            throw new RatingInvalidException("Rating must be between 1 and 5");
        }

        Rating rating = Rating.builder()
                .rating(foodRatingRequest.getRating())
                .food(food)
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

    @Transactional
    public ResponseEntity<?> orderFood(String currentCustomerEmail, OrderFoodRequest orderFoodRequest) {

        Customer customer = getCustomerByEmail(currentCustomerEmail);

        Food food = foodService.getFoodById(orderFoodRequest.getFoodId());
        Restaurant restaurant = food.getMenu().getRestaurant();

        long now = new Date().getTime();
        Order order = Order.builder()
                .food(food)
                .customer(customer)
                .restaurant(restaurant)
                .status(OrderStatus.READY)
                .orderTime(LocalDateTime.now())
                .quantity(orderFoodRequest.getQuantity())
                .build();

        restaurant.getOrders().add(order);
        restaurantService.saveRestaurant(restaurant);
        orderService.saveOrder(order);

        OrderFoodResponse orderFoodResponse = OrderFoodResponse.builder()
                .foodName(food.getName())
                .orderId(order.getId())
                .foodId(order.getFoodId())
                .restaurantId(order.getRestaurantId())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .build();

        return ResponseEntity.ok(orderFoodResponse);
    }

    public List<?> getFoodComments(Long foodId) {
        Food food = foodService.getFoodById(foodId);
        return food.getComments();
    }

    public List<?> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<?> findAllUnDeliveredOrders(long customerId) {
        // check if customer exists
        Customer customer = getCustomerById(customerId);

        return customerRepository.findAllDeliveryByStatus(customerId,  EnumSet.of(OrderStatus.READY, OrderStatus.DELIVERING)); // find ready or delivering orders that belong to the customer
    }

    private Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
    }


}
