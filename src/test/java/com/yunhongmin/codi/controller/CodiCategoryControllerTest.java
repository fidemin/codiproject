package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.common.CommonResponseDto;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.dto.CategoryWithMinMaxBrandsDto;
import com.yunhongmin.codi.dto.CodiBrandPriceDto;
import com.yunhongmin.codi.exception.BadRequestException;
import com.yunhongmin.codi.exception.CodiCategoryException;
import com.yunhongmin.codi.service.CodiCategoryService;
import com.yunhongmin.codi.validator.CodiCategoryValidator;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CodiCategoryControllerTest {
    @InjectMocks
    private CodiCategoryController codiCategoryController;

    @Mock
    private CodiCategoryValidator codiCategoryValidator;
    @Mock
    private CodiCategoryService codiCategoryService;

    @Test
    @DisplayName("getCategoryWithMinMaxPriceBrands method 성공 케이스")
    void getCategoryWithMinMaxPriceBrands() {
        // given
        CodiCategory category = CodiCategory.TOP;
        List<CodiBrandPriceDto> minPrice = new ArrayList<>();
        List<CodiBrandPriceDto> maxPrice = new ArrayList<>();
        CategoryWithMinMaxBrandsDto dto = CategoryWithMinMaxBrandsDto
                .builder()
                .category(category).maxPrice(maxPrice).minPrice(minPrice)
                .build();
        String categoryString = "top";

        when(codiCategoryService.getCategoryWithMinMaxBrands(CodiCategory.TOP)).thenReturn(dto);

        // when
        ResponseEntity<CommonResponseDto<CategoryWithMinMaxBrandsDto>> responseEntity
                = codiCategoryController.getCategoryWithMinMaxPriceBrands(categoryString);
        CommonResponseDto<CategoryWithMinMaxBrandsDto> body = responseEntity.getBody();

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, body.getSuccess());
        assertNull(body.getErrorMessage());
        CategoryWithMinMaxBrandsDto actualDto = body.getData();
        assertEquals(dto, actualDto);

        verify(codiCategoryValidator, times(1)).validateCategory(categoryString);
    }

    @Test
    @DisplayName("getCategoryWithMinMaxPriceBrands method validation fail 케이스")
    void getCategoryWithMinMaxPriceBrands_validationFail() {
        // given
        doThrow(CodiCategoryException.class).when(codiCategoryValidator).validateCategory("top1");

        // expected
        assertThrows(BadRequestException.class, () ->
                codiCategoryController.getCategoryWithMinMaxPriceBrands("top1"));

    }
}