package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.common.CommonResponseDto;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.dto.CategoryWithMinMaxBrandsDto;
import com.yunhongmin.codi.service.CodiCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "api/codi/categories")
@RequiredArgsConstructor
public class CodiCategoryController {

    private final CodiCategoryService codiCategoryService;

    @RequestMapping(method = RequestMethod.GET, value = "/{category}/withMinMaxPrice")
    public ResponseEntity<CommonResponseDto<CategoryWithMinMaxBrandsDto>> getCategoryWithMinMaxPriceBrands(
            @PathVariable("category") String category) {

        String categoryString = category.toUpperCase();
        CodiCategory codiCategory = null;
        try {
            codiCategory = CodiCategory.valueOf(categoryString);
        } catch (IllegalArgumentException ignored) {
        }

        if (Objects.isNull(codiCategory)) {
            String errorMessage = String.format("%s is not proper category.", category);
            return new ResponseEntity<>(CommonResponseDto.ofFail(errorMessage), HttpStatus.BAD_REQUEST);
        }
        CategoryWithMinMaxBrandsDto categoryWithMinMaxBrandsDto =
                codiCategoryService.getCategoryWithMinMaxBrandsDto(codiCategory);
        return ResponseEntity.ok(CommonResponseDto.ofSuccess(categoryWithMinMaxBrandsDto));
    }
}
