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
            food.setMenu(menu);
        }
        return menuRepository.save(menu);
    }

    public Food editFood(Long foodId, Food newFood, Menu menu) {

        for (Food food : menu.getFoods()) {
            //check if new fields are not null
            if (food.getId() == foodId) {
                if (newFood.getName() != null) {
                    food.setName(newFood.getName());
                }

                if (newFood.getPrice() > 0) {
                    food.setPrice(newFood.getPrice());
                }

                if (newFood.getDescription() != null) {
                    food.setDescription(newFood.getDescription());
                }

                if (newFood.getImageUrl() != null) {
                    food.setImageUrl(newFood.getImageUrl());
                }
                return foodService.saveFood(food);
            }
        }
        return null;
    }
}
