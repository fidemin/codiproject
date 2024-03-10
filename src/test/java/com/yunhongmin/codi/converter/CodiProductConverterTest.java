package com.yunhongmin.codi.converter;

import com.yunhongmin.codi.domain.CodiBrand;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.domain.CodiProduct;
import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodiProductConverterTest {

    @Test
    void toCodiProductWithBrandNameDto() {
        CodiBrand codiBrand = new CodiBrand();
        codiBrand.setName("A");
        CodiProduct codiProduct = new CodiProduct();
        codiProduct.setCodiBrand(codiBrand);
        codiProduct.setCodiCategory(CodiCategory.BAG);
        codiProduct.setPrice(1000);


        CodiProductWithBrandNameDto codiProductWithBrandNameDto =
                CodiProductConverter.toCodiProductWithBrandNameDto(codiProduct);

        assertEquals("A", codiProductWithBrandNameDto.getBrandName());
        assertEquals("BAG", codiProductWithBrandNameDto.getCategory());
        assertEquals(1000, codiProductWithBrandNameDto.getPrice());
    }
}