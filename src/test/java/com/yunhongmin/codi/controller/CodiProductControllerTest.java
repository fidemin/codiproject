package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.common.CommonResponseDto;
import com.yunhongmin.codi.dto.CodiProductWithBrandNameDto;
import com.yunhongmin.codi.dto.MinPriceByCategoryDto;
import com.yunhongmin.codi.service.CodiProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CodiProductControllerTest {
    @InjectMocks
    private CodiProductController codiProductController;

    @Mock
    private CodiProductService codiProductService;

    @Test
    @DisplayName("findProductsWithMinPriceByCategory method 성공 케이스")
    void findProductsWithMinPriceByCategory() {
        // given
        CodiProductWithBrandNameDto dto1 = CodiProductWithBrandNameDto
                .builder()
                .brandName("A")
                .category("top")
                .price(1000)
                .build();
        CodiProductWithBrandNameDto dto2 = CodiProductWithBrandNameDto
                .builder()
                .brandName("B")
                .category("sneakers")
                .price(20000)
                .build();
        List<CodiProductWithBrandNameDto> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);

        when(codiProductService.findDistinctProductsWithMinPriceByCategory()).thenReturn(list);

        // when
        ResponseEntity<CommonResponseDto<MinPriceByCategoryDto>> responseEntity
                = codiProductController.findProductsWithMinPriceByCategory();

        // then
        CommonResponseDto<MinPriceByCategoryDto> body = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, body.getSuccess());
        assertNull(body.getErrorMessage());
        MinPriceByCategoryDto data = body.getData();
        List<CodiProductWithBrandNameDto> minPriceByCategory = data.getMinPriceByCategory();
        assertEquals(list, minPriceByCategory);
        assertEquals(21000, data.getTotalPrice());
    }
}