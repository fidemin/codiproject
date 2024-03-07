package com.yunhongmin.codi.dto;

import com.yunhongmin.codi.domain.CodiCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodiCategoryPriceDto {
    private CodiCategory category;
    private int price;
}
