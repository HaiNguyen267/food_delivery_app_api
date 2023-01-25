package com.example.lesson3_food_delivery_app_api.dto.response;

import com.example.lesson3_food_delivery_app_api.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersResponse {
    //TODO: delete this class

    @Data
    public static class OrderDTO {
        private Long id;
        private double price;
        private Long foodId;
        private Long deliveryPartnerId;
        private String status;

        public OrderDTO(Order order) {
            this.id = order.getId();
            this.price = order.getPrice();
            this.foodId = order.getFood().getId();
            // if the order is being delivering by a delivery partner, then we return the delivery partner id
            if (order.getDeliveryPartner() != null) {
                this.deliveryPartnerId = order.getDeliveryPartner().getId();
            }
            this.status = order.getStatus().name();
        }
    }

    private List<OrderDTO> orders;
}
