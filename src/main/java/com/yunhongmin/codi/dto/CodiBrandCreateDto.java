package com.yunhongmin.codi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CodiBrandCreateDto {
    private String brandName;
    private List<CodiCategoryPriceDto> categories;
}
