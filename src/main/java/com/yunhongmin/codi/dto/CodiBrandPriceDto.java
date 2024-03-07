package com.yunhongmin.codi.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CodiBrandPriceDto {
    private final long brandId;
    private final String brandName;
    private final int price;
}
