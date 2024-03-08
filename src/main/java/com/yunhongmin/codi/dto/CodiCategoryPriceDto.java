package com.yunhongmin.codi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodiCategoryPriceDto {
    private String category;
    private int price;
}
