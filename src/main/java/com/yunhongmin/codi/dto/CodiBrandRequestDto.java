package com.yunhongmin.codi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CodiBrandRequestDto {
    private String brandName;
    private List<CodiCategoryPriceDto> categories;
}
