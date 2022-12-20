package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.GetOrdersResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.RegisterResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.jwt.JwtProvider;
import com.example.lesson3_food_delivery_app_api.repository.RestaurantRepository;
import com.example.lesson3_food_delivery_app_api.repository.UserRepository;
import com.example.lesson3_food_delivery_app_api.security.Role;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final PasswordEncoder passwordEncoder;

    private final MenuService menuService;

    private final FoodService foodService;

    private final UserRepository userRepository;

    private final OrderService orderService;

    private final EventLogService eventLogService;

    private final UserService userService;

    public ResponseEntity<?> register(RestaurantRegistrationRequest registrationRequest) {
        return userService.registerRestaurant(registrationRequest);
    }

    public ResponseEntity<?> addMenu(String restaurantEmail, Menu menu) {
//        System.out.println("jihi");

        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);

        menu.getFoods().forEach(food -> food.setMenu(menu)); // food is the owning side in the bidirectional relationship with menu

        restaurant.setMenu(menu); // restaurant is the owning side in the bidirectional relationship with menu
        restaurantRepository.save(restaurant);

        SuccessResponse response = new SuccessResponse("Menu added successfully");
        eventLogService.saveEventLog(EventLog.Event.EDIT_MENU, restaurant.getId());

        return ResponseEntity.ok(menu);
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
        eventLogService.saveEventLog(EventLog.Event.EDIT_MENU, restaurant.getId());
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

        // there is a bidirectional relationship between menu and food that prevents Hibernate from deleting the food
        // remove the bidirectional relationship, so that Hibernate can delete the food from database
        Food food = foodService.getFoodById(foodId);
        Menu menu = food.getMenu();

        menu.getFoods().remove(food); // menu now doesn't have the food
        food.setMenu(null); // the food now loses the reference to the menu

        menuService.saveMenu(menu);
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
        } else {
            restaurant.getMenu().getFoods().add(food);
        }

        food.setMenu(restaurant.getMenu());

        restaurant = restaurantRepository.save(restaurant);
        SuccessResponse message = new SuccessResponse("Food added successfully");
        eventLogService.saveEventLog(EventLog.Event.EDIT_MENU, restaurant.getId());
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

    public ResponseEntity<?> getMenu(String restaurantEmail) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        return ResponseEntity.ok().body(restaurant.getMenu());
    }

    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public List<?> getRestaurantOrders(long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        return restaurant.getOrders();
    }

    private Restaurant getRestaurantById(long restaurantId) {
        //TODO: NOT FOUND EXCEPTION
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}
