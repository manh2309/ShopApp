package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order id phai lon hon 0")
    private Long orderId;
    @JsonProperty("product_id")
    @Min(value = 1, message = "product id phai lon hon 0")
    private Long productId;
    @Min(value = 0, message = "so tien phai >= 0")
    private Long price;
    @JsonProperty("number_of_products")
    @Min(value = 1, message = "so luong phai lon hon 0")
    private Long numberOfProducts;
    @JsonProperty("total_money")
    @Min(value = 0, message = "so tien phai >= 0")
    private Long totalMoney;
    private String color;
}
