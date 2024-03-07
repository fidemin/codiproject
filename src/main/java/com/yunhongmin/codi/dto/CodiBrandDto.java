package com.yunhongmin.codi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CodiBrandDto {
    private long brandId;
    private String brandName;
    private List<CodiCategoryPriceDto> categories;
    private int totalPrice;
}
