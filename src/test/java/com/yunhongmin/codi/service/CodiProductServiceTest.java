package com.yunhongmin.codi.service;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.domain.StatType;
import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;
import com.yunhongmin.codi.repository.CodiProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodiProductServiceTest {
    @InjectMocks
    private CodiProductService codiProductService;

    @Mock
    private CodiProductRepository codiProductRepository;

    @Test
    void findDistinctProductsWithMinPriceByCategory() {
        // given
        CodiBrand codiBrand1 = new CodiBrand();
        codiBrand1.setName("A");

        CodiBrand codiBrand2 = new CodiBrand();
        codiBrand1.setName("B");

        CodiProduct codiProduct1 = new CodiProduct();
        codiProduct1.setCodiBrand(codiBrand1);
        codiProduct1.setCodiCategory(CodiCategory.BAG);
        codiProduct1.setPrice(1000);

        // duplicate category
        CodiProduct codiProduct2 = new CodiProduct();
        codiProduct2.setCodiBrand(codiBrand2);
        codiProduct2.setCodiCategory(CodiCategory.BAG);
        codiProduct2.setPrice(1000);

        CodiProduct codiProduct3 = new CodiProduct();
        codiProduct3.setCodiBrand(codiBrand1);
        codiProduct3.setCodiCategory(CodiCategory.TOP);
        codiProduct3.setPrice(10000);

        List<CodiProduct> codiProducts = new ArrayList<>();
        codiProducts.add(codiProduct1);
        codiProducts.add(codiProduct2);
        codiProducts.add(codiProduct3);

        when(codiProductRepository.findCodiProductWithStatType(StatType.MIN)).thenReturn(codiProducts);

        // when
        List<CodiProductWithBrandNameDto> actual
                = codiProductService.findDistinctProductsWithMinPriceByCategory();

        // then
        assertEquals(2, actual.size());

        assertEquals(codiProduct1.getCodiCategory().toString(), actual.get(0).getCategory());
        assertEquals(codiProduct1.getCodiBrand().getName(), actual.get(0).getBrandName());
        assertEquals(codiProduct1.getPrice(), actual.get(0).getPrice());

        assertEquals(codiProduct3.getCodiCategory().toString(), actual.get(1).getCategory());
        assertEquals(codiProduct3.getCodiBrand().getName(), actual.get(1).getBrandName());
        assertEquals(codiProduct3.getPrice(), actual.get(1).getPrice());
    }
}