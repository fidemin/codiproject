package com.yunhongmin.codi.dto;

import com.yunhongmin.codi.domain.CodiCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryWithMinMaxBrandsDto {
    private CodiCategory category;
    private List<CodiBrandPriceDto> minPrice;
    private List<CodiBrandPriceDto> maxPrice;
}
