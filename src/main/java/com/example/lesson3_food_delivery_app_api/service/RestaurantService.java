package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.GetOrdersResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.entity.Menu;
import com.example.lesson3_food_delivery_app_api.entity.Order;
import com.example.lesson3_food_delivery_app_api.entity.Restaurant;
import com.example.lesson3_food_delivery_app_api.jwt.JwtProvider;
import com.example.lesson3_food_delivery_app_api.repository.RestaurantRepository;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.lesson3_food_delivery_app_api.dto.response.GetOrdersResponse.*;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuService menuService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    public ResponseEntity<?> register(RestaurantRegistrationRequest registrationRequest) {
        String restaurantEmail = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();
        String address = registrationRequest.getAddress();
        String phone = registrationRequest.getPhone();
        String name = registrationRequest.getName();

        if (userRepository.existsByEmailIgnoreCase(restaurantEmail)) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .build();

        restaurant.setEmail(restaurantEmail);
        restaurant.setPassword(passwordEncoder.encode(password));
        restaurant.setRole(Role.RESTAURANT);

        restaurant = restaurantRepository.save(restaurant);

        return AuthService.createResponseWithAccessToken(restaurant);
    }

    public ResponseEntity<?> addMenu(String restaurantEmail, Menu menu) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        menuService.saveMenu(menu);
        restaurant.setMenu(menu);

        restaurantRepository.save(restaurant);
        SuccessResponse response = new SuccessResponse("Menu added successfully");
        return ResponseEntity.ok(response);
    }

    private Restaurant getRestaurantByEmail(String restaurantEmail) {
        return restaurantRepository.findByEmail(restaurantEmail)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public ResponseEntity<?> editMenu(String restaurantEmail, Long foodId, Food food) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        if (!checkIfFoodIdExistsInMenu(foodId, restaurant)) {
            ErrorResponse message = new ErrorResponse("Food id does not exist in menu");
            return ResponseEntity.badRequest().body(message);
        }

        Menu menu = restaurant.getMenu();
        menuService.editFood(foodId, food, menu);

        SuccessResponse message = new SuccessResponse("Food edited successfully");
        return ResponseEntity.ok().body(message);
    }

    private boolean checkIfFoodIdExistsInMenu(Long foodId, Restaurant restaurant) {
        return restaurant.getMenu().getFoods().stream()
                .anyMatch(food -> food.getId().equals(foodId));
    }

    public ResponseEntity<?> deleteFood(String restaurantEmail, Long foodId) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        if (!checkIfFoodIdExistsInMenu(foodId, restaurant)) {
            ErrorResponse message = new ErrorResponse("Food id does not exist in menu");
            return ResponseEntity.badRequest().body(message);
        }

        foodService.deleteFoodById(foodId);

        SuccessResponse message = new SuccessResponse("Food deleted successfully");
        return ResponseEntity.ok().body(message);
    }

    @Transactional
    public ResponseEntity<?> addFood(String restaurantEmail, Food food) {

        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);


        // if the restaurant does not have a menu, create a new one and add the food to it
        if (restaurant.getMenu() == null) {
            Menu menu = new Menu();
            menu.getFoods().add(food);

            restaurant.setMenu(menu);
        }

        restaurant.getMenu().getFoods().add(food);

        restaurantRepository.save(restaurant);
        SuccessResponse message = new SuccessResponse("Food added successfully");
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<?> getOrders(String restaurantEmail) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);

        List<Order> orders = restaurant.getOrders();

        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderDTO::new)
                .toList();

        GetOrdersResponse response = new GetOrdersResponse(orderDTOs);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> trackOrder(String restaurantEmail, Long orderId) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);

        if (!checkIfOrderExistsInOrderList(orderId, restaurant)) {
            ErrorResponse message = new ErrorResponse("Order not found in order list");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(order);
    }

    private boolean checkIfOrderExistsInOrderList(Long orderId, Restaurant restaurant) {
        return restaurant.getOrders().stream()
                .anyMatch(order -> order.getId().equals(orderId));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public ResponseEntity<?> getRestaurantMenu(long restaurantId) {

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalRestaurant.isEmpty()) {
            ErrorResponse response = new ErrorResponse("Restaurant not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Menu menu = optionalRestaurant.get().getMenu();
        return ResponseEntity.ok().body(menu);
    }
}
