package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiCategoryStat;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.domain.StatType;
import com.yunhongmin.codi.dto.CategoryWithMinMaxBrandsDto;
import com.yunhongmin.codi.dto.CodiBrandPriceDto;
import com.yunhongmin.codi.repository.CodiCategoryStatRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CodiCategoryService {
    private final CodiProductRepository codiProductRepository;
    private final CodiCategoryStatRepository codiCategoryStatRepository;


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

    @Transactional
    public void updateCodiCategoryStats() {
        // Delete and create new strategy
        StatType[] statTypes = new StatType[]{StatType.MAX, StatType.MIN};
        codiCategoryStatRepository.deleteBulkByStatTypes(Arrays.stream(statTypes).toList());

        List<CodiCategoryStat> codiCategoryStatMins = codiProductRepository.findCodiCategoryStatMin();
        for (CodiCategoryStat codiCategoryStatMin : codiCategoryStatMins) {
            codiCategoryStatRepository.save(codiCategoryStatMin);
        }

        List<CodiCategoryStat> codiCategoryStatMaxs = codiProductRepository.findCodiCategoryStatMax();
        for (CodiCategoryStat codiCategoryStatMax : codiCategoryStatMaxs) {
            codiCategoryStatRepository.save(codiCategoryStatMax);
        }
    }
}
