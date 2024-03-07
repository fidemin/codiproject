package com.yunhongmin.codi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodiProductWithBrandNameDto {
    private String brandName;
    private String category;
    private int price;
}
