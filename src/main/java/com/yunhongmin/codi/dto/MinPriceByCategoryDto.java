package com.yunhongmin.codi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class MinPriceByCategoryDto {
    private final List<CodiProductWithBrandNameDto> minPriceByCategory;
    private final int totalPrice;
}
