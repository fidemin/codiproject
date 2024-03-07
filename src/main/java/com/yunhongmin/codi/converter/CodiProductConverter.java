package com.yunhongmin.codi.converter;

import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;

public class CodiProductConverter {
    public static CodiProductWithBrandNameDto toCodiProductWithBrandNameDto(CodiProduct codiProduct) {
        return CodiProductWithBrandNameDto.builder()
                .brandName(codiProduct.getCodiBrand().getName())
                .price(codiProduct.getPrice())
                .category(codiProduct.getCodiCategory().toString())
                .build();
    }
}
