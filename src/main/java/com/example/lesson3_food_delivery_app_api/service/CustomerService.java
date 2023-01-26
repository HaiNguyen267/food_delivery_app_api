package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.CustomerRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.FoodCommentRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.FoodRatingRequest;
import com.example.lesson3_food_delivery_app_api.dto.request.OrderFoodRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.FoodOrderDTO;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.exception.NotFoundException;
import com.example.lesson3_food_delivery_app_api.exception.RatingInvalidException;
import com.example.lesson3_food_delivery_app_api.exception.UserHasNotOrderedFoodException;
import com.example.lesson3_food_delivery_app_api.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final FoodService foodService;
    private final RestaurantService restaurantService;
    private final EventLogService eventLogService;
    private final OrderService orderService;
    private final UserService userService;

    public ResponseEntity<?> register(CustomerRegistrationRequest customerRegistrationRequest) {
        return userService.registerCustomer(customerRegistrationRequest);

    }

    public ResponseEntity<?> viewAllFoods() {
        List<Food> allFoods = foodService.getAllFoods();
        SuccessResponse response = new SuccessResponse(200, "All foods retrieved successfully", allFoods);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?>  viewAllRestaurants() {
        List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
        SuccessResponse response = new SuccessResponse(200, "All restaurants retrieved successfully", allRestaurants);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getRestaurantMenu(long restaurantId) {
        Menu restaurantMenu = restaurantService.getRestaurantMenu(restaurantId);
        SuccessResponse response = new SuccessResponse(200, "Restaurant menu retrieved successfully", restaurantMenu.getFoods());
        return ResponseEntity.ok(response);
    }

    @Transactional
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
        eventLogService.saveEventLog(EventLog.Event.COMMENT_FOOD, customer.getId());
        SuccessResponse response = new SuccessResponse(200,"Commented successfully");
        return ResponseEntity.ok(response);
    }

    private Customer getCustomerByEmail(String userEmail) {
        return customerRepository.findByEmail(userEmail);
    }



    @Transactional
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

        eventLogService.saveEventLog(EventLog.Event.RATE_FOOD, customer.getId());
        SuccessResponse response = new SuccessResponse(200, "Rated successfully");
        return ResponseEntity.ok(response);
    }

    private boolean isRatingValid(Integer rating) {
        return rating >= 1 && rating <= 5;
    }

    private boolean checkIfUserHasOrderedFood(Customer customer, Food food) {
        return customer.getOrders().stream().anyMatch(order -> order.getFood().getId().equals(food.getId()));
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
        orderService.saveOrder(order);
        eventLogService.saveEventLog(EventLog.Event.ORDER_FOOD, customer.getId());

        FoodOrderDTO orderFoodResponse = FoodOrderDTO.builder()
                .foodName(food.getName())
                .orderId(order.getId())
                .foodId(order.getFood().getId())
                .restaurantName(order.getRestaurantName())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .build();

        SuccessResponse response = new SuccessResponse(200, "Food ordered successfully", orderFoodResponse);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getFoodComments(Long foodId) {
        Food food = foodService.getFoodById(foodId);
        List<Comment> comments = food.getComments();
        SuccessResponse successResponse = new SuccessResponse(200, "View food comments successfully", comments);
        return ResponseEntity.ok(successResponse);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Order> findAllUnDeliveredOrders(long customerId) {
        // check if customer exists
        Customer customer = getCustomerById(customerId);

        return customerRepository.findAllDeliveryByStatus(customerId,  EnumSet.of(OrderStatus.READY, OrderStatus.DELIVERING)); // find ready or delivering orders that belong to the customer
    }

    private Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
    }


    public ResponseEntity<?> getOrders(String currentCustomerEmail) {
        List<Order> unDeliveredOrdersOfCustomer = orderService.findUnDeliveredOrdersOfCustomer(currentCustomerEmail);
        SuccessResponse response = new SuccessResponse(200, "Orders retrieved successfully", unDeliveredOrdersOfCustomer);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> searchFood(String name) {
        List<Food> foods = foodService.getFoodsByNameContaining(name);

        if (foods.size() == 0) {
            ErrorResponse response = new ErrorResponse(404, "No results");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            SuccessResponse response = new SuccessResponse(200, "Search food successfully", foods);
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<?> searchRestaurant(String name) {
        List<Restaurant> restaurants =  restaurantService.getRestaurantsByNameContaining(name);
        if (restaurants.size() == 0) {
            ErrorResponse response = new ErrorResponse(404, "No results");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            SuccessResponse response = new SuccessResponse(200, "Search restaurant successfully", restaurants);
            return ResponseEntity.ok(response);
        }
    }
}
