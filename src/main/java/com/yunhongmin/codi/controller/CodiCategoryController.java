package com.yunhongmin.codi.controller;

import com.yunhongmin.codi.common.CommonResponseDto;
import com.yunhongmin.codi.domain.CodiCategory;
import com.yunhongmin.codi.dto.CategoryWithMinMaxBrandsDto;
import com.yunhongmin.codi.exception.BadRequestException;
import com.yunhongmin.codi.exception.CodiCategoryException;
import com.yunhongmin.codi.service.CodiCategoryService;
import com.yunhongmin.codi.validator.CodiCategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/codi/categories")
@RequiredArgsConstructor
@Slf4j
public class CodiCategoryController {

    private final CodiCategoryService codiCategoryService;
    private final CodiCategoryValidator codiCategoryValidator;

    @RequestMapping(method = RequestMethod.GET, value = "/{category}/withMinMaxPrice")
    public ResponseEntity<CommonResponseDto<CategoryWithMinMaxBrandsDto>> getCategoryWithMinMaxPriceBrands(
            @PathVariable("category") String category) {


        try {
            codiCategoryValidator.validateCategory(category);
        } catch (CodiCategoryException ex) {
            log.warn(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }

        CodiCategory codiCategory = CodiCategory.valueOf(category.toUpperCase());
        CategoryWithMinMaxBrandsDto categoryWithMinMaxBrandsDto =
                codiCategoryService.getCategoryWithMinMaxBrands(codiCategory);
        return ResponseEntity.ok(CommonResponseDto.ofSuccess(categoryWithMinMaxBrandsDto));
    }
}
