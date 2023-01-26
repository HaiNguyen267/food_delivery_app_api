package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.dto.request.RestaurantRegistrationRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.FoodOrderDTO;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.entity.*;
import com.example.lesson3_food_delivery_app_api.exception.NotFoundException;
import com.example.lesson3_food_delivery_app_api.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final MenuService menuService;

    private final FoodService foodService;

    private final OrderService orderService;

    private final EventLogService eventLogService;

    private final UserService userService;

    public ResponseEntity<?> register(RestaurantRegistrationRequest registrationRequest) {
        return userService.registerRestaurant(registrationRequest);
    }

    public ResponseEntity<?> addMenu(String restaurantEmail, Menu menu) {

        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);

        menu.getFoods().forEach(food -> food.setMenu(menu)); // food is the owning side in the bidirectional relationship with menu

        // if the restaurant has no menu, the menu is set as the restaurant's menu
        if (restaurant.getMenu() == null) {
            restaurant.setMenu(menu); // restaurant is the owning side in the bidirectional relationship with menu
        } else {
            // otherwise, the food in the menu is added to the restaurant's menu
            restaurant.getMenu().getFoods().addAll(menu.getFoods());
        }
        restaurantRepository.save(restaurant);

        SuccessResponse response = new SuccessResponse(200, "Menu added successfully");
        eventLogService.saveEventLog(EventLog.Event.EDIT_MENU, restaurant.getId());

        return ResponseEntity.ok(response);
    }

    private Restaurant getRestaurantByEmail(String restaurantEmail) {
        return (Restaurant) restaurantRepository.findByEmail(restaurantEmail)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public ResponseEntity<?> editMenu(String restaurantEmail, Long foodId, Food food) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        if (!checkIfFoodIdExistsInMenu(foodId, restaurant)) {
            throw new NotFoundException("Food id does not exist in menu");

        }

        Menu menu = restaurant.getMenu();
        Food editedFood = menuService.editFood(foodId, food, menu);

        eventLogService.saveEventLog(EventLog.Event.EDIT_MENU, restaurant.getId());

        SuccessResponse message = new SuccessResponse(200, "Food edited successfully", editedFood);
        return ResponseEntity.ok().body(message);
    }

    private boolean checkIfFoodIdExistsInMenu(Long foodId, Restaurant restaurant) {
        return restaurant.getMenu().getFoods().stream()
                .anyMatch(food -> food.getId().equals(foodId));
    }

    public ResponseEntity<?> deleteFood(String restaurantEmail, Long foodId) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        if (!checkIfFoodIdExistsInMenu(foodId, restaurant)) {
            throw new NotFoundException("Food id does not exist in menu");
        }

        // there is a bidirectional relationship between menu and food that prevents Hibernate from deleting the food
        // remove the bidirectional relationship, so that Hibernate can delete the food from database
        Food food = foodService.getFoodById(foodId);
        Menu menu = food.getMenu();

        menu.getFoods().remove(food); // menu now doesn't have the food
        food.setMenu(null); // the food now loses the reference to the menu

        menuService.saveMenu(menu);
        foodService.deleteFoodById(foodId);

        SuccessResponse message = new SuccessResponse(200, "Food deleted successfully");
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
        SuccessResponse message = new SuccessResponse(200, "Food added successfully");
        eventLogService.saveEventLog(EventLog.Event.EDIT_MENU, restaurant.getId());
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<?> getOrders(String restaurantEmail) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        List<Order> orders = restaurant.getOrders();

        List<FoodOrderDTO> orderDTOs = orders.stream()
                .map(FoodOrderDTO::new)
                .toList();

        SuccessResponse response = new SuccessResponse(200, "Orders retrieved successfully", orderDTOs);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> trackOrder(String restaurantEmail, Long orderId) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);

        if (!checkIfOrderExistsInOrderList(orderId, restaurant)) {
            throw new NotFoundException("Order not found in order list");

        }

        Order order = orderService.getOrderById(orderId);

        SuccessResponse response = new SuccessResponse(200, "Order retrieved successfully", order);
        return ResponseEntity.ok().body(response);
    }

    private boolean checkIfOrderExistsInOrderList(Long orderId, Restaurant restaurant) {
        return restaurant.getOrders().stream()
                .anyMatch(order -> order.getId().equals(orderId));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Menu getRestaurantMenu(long restaurantId) {

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant not found");
        }

        Restaurant restaurant = optionalRestaurant.get();
        return restaurant.getMenu();

    }

    public ResponseEntity<?> getMenu(String restaurantEmail) {
        Restaurant restaurant = getRestaurantByEmail(restaurantEmail);
        Menu menu = restaurant.getMenu();

        SuccessResponse response = new SuccessResponse(200, "Menu retrieved successfully", menu.getFoods());
        return ResponseEntity.ok().body(response);
    }

    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public List<Order> getRestaurantOrders(long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        return restaurant.getOrders();
    }

    private Restaurant getRestaurantById(long restaurantId) {
        return (Restaurant) restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
    }

    public List<Restaurant> getRestaurantsByNameContaining(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }
}
