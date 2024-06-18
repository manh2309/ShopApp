package com.example.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriDTO {
    @NotEmpty(message = "Khong de trong")
    private String name;
}
