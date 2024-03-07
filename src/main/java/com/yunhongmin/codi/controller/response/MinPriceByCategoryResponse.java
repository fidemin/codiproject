package com.yunhongmin.codi.controller.response;

import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class MinPriceByCategoryResponse {
    private final List<CodiProductWithBrandNameDto> minPriceByCategory;
    private final int totalPrice;
}
