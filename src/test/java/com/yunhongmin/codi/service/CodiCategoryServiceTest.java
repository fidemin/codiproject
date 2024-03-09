package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.*;
import com.yunhongmin.codi.dto.CategoryWithMinMaxBrandsDto;
import com.yunhongmin.codi.repository.CodiCategoryStatRepository;
import com.yunhongmin.codi.repository.CodiProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodiCategoryServiceTest {
    @InjectMocks
    private CodiCategoryService codiCategoryService;

    @Mock
    private CodiProductRepository codiProductRepository;

    @Mock
    private CodiCategoryStatRepository codiCategoryStatRepository;


    @Test
    void getCategoryWithMinMaxBrandsDto() {
        // given
        CodiBrand codiBrand1 = new CodiBrand();
        codiBrand1.setId(1L);
        codiBrand1.setName("A");

        CodiProduct codiProduct1 = new CodiProduct();
        codiProduct1.setCodiBrand(codiBrand1);
        codiProduct1.setCodiCategory(CodiCategory.BAG);
        codiProduct1.setPrice(1000);

        List<CodiProduct> minProducts = new ArrayList<>();
        minProducts.add(codiProduct1);

        when(codiProductRepository.findByStatTypeAndCategory(StatType.MIN, CodiCategory.BAG)).thenReturn(minProducts);

        CodiBrand codiBrand2 = new CodiBrand();
        codiBrand2.setId(2L);
        codiBrand2.setName("B");

        CodiProduct codiProduct2 = new CodiProduct();
        codiProduct2.setCodiBrand(codiBrand2);
        codiProduct2.setCodiCategory(CodiCategory.BAG);
        codiProduct2.setPrice(1000);

        List<CodiProduct> maxProducts = new ArrayList<>();
        maxProducts.add(codiProduct2);

        when(codiProductRepository.findByStatTypeAndCategory(StatType.MAX, CodiCategory.BAG)).thenReturn(maxProducts);

        // when
        CategoryWithMinMaxBrandsDto actual = codiCategoryService.getCategoryWithMinMaxBrands(CodiCategory.BAG);

        // then
        assertEquals(CodiCategory.BAG, actual.getCategory());
        assertEquals(1, actual.getMinPrice().size());
        assertEquals(codiProduct1.getCodiBrand().getId(), actual.getMinPrice().get(0).getBrandId());
        assertEquals(codiProduct1.getCodiBrand().getName(), actual.getMinPrice().get(0).getBrandName());
        assertEquals(codiProduct1.getPrice(), actual.getMinPrice().get(0).getPrice());

        assertEquals(1, actual.getMaxPrice().size());
        assertEquals(codiProduct2.getCodiBrand().getId(), actual.getMaxPrice().get(0).getBrandId());
        assertEquals(codiProduct2.getCodiBrand().getName(), actual.getMaxPrice().get(0).getBrandName());
        assertEquals(codiProduct2.getPrice(), actual.getMaxPrice().get(0).getPrice());
    }

    @Test
    void updateCodiCategoryStats() {
        // given
        List<CodiCategoryStat> min = new ArrayList<>();
        min.add(new CodiCategoryStat());
        min.add(new CodiCategoryStat());
        when(codiProductRepository.findCodiCategoryStatMin()).thenReturn(min);

        List<CodiCategoryStat> max = new ArrayList<>();
        max.add(new CodiCategoryStat());
        when(codiProductRepository.findCodiCategoryStatMax()).thenReturn(max);

        // when
        codiCategoryService.updateCodiCategoryStats();

        // then
        verify(codiCategoryStatRepository, times(1)).deleteBulkByStatTypes(any());
        verify(codiCategoryStatRepository, times(min.size() + max.size())).save(any());
    }
}