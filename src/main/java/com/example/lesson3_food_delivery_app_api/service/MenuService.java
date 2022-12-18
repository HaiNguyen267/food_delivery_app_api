package com.example.lesson3_food_delivery_app_api.service;

import com.example.lesson3_food_delivery_app_api.entity.Food;
import com.example.lesson3_food_delivery_app_api.entity.Menu;
import com.example.lesson3_food_delivery_app_api.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final FoodService foodService;

    @Transactional
    public Menu saveMenu(Menu menu) {
        for (Food food : menu.getFoods()) {
            foodService.saveFood(food);
        }
        return menuRepository.save(menu);
    }

    public void editFood(Long foodId, Food newFood, Menu menu) {

        for (Food food : menu.getFoods()) {
            if (food.getId() == foodId) {
                food.setName(newFood.getName());
                food.setPrice(newFood.getPrice());
                food.setDescription(newFood.getDescription());
                food.setImageUrl(newFood.getImageUrl());
                foodService.saveFood(food);
            }
        }

    }
}
