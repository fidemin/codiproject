package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.domain.StatType;
import com.yunhongmin.codi.dto.CategoryWithMinMaxBrandsDto;
import com.yunhongmin.codi.dto.CodiBrandPriceDto;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodiCategoryService {
    private final CodiProductRepository codiProductRepository;


    public CategoryWithMinMaxBrandsDto getCategoryWithMinMaxBrandsDto(CodiCategory category) {
        List<CodiProduct> codiProductsWithMins = codiProductRepository.findByStatTypeAndCategory(StatType.MIN, category);
        List<CodiBrandPriceDto> codiBrandPriceMinDtos = codiProductsWithMins.stream()
                .map(cp -> new CodiBrandPriceDto(
                        cp.getCodiBrand().getId(), cp.getCodiBrand().getName(), cp.getPrice()))
                .toList();
        List<CodiProduct> codiProductsWithMaxs = codiProductRepository.findByStatTypeAndCategory(StatType.MAX, category);
        List<CodiBrandPriceDto> codiBrandPriceMaxDtos = codiProductsWithMaxs.stream()
                .map(cp -> new CodiBrandPriceDto(
                        cp.getCodiBrand().getId(), cp.getCodiBrand().getName(), cp.getPrice()))
                .toList();

        return CategoryWithMinMaxBrandsDto.builder()
                .category(category)
                .minPrice(codiBrandPriceMinDtos)
                .maxPrice(codiBrandPriceMaxDtos).build();
    }
}
