package com.yunhongmin.codi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodiCategoryPriceDto {
    @NotEmpty(message = "category should not be empty")
    @NotNull
    private String category;
    @Positive(message = "price must be positive")
    private int price;
}
